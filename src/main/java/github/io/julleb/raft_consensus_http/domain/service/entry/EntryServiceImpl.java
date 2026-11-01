package github.io.julleb.raft_consensus_http.domain.service.entry;

import static github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation.SEND_ENTRIES_MILLISECOND_INTERVAL;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.model.EntryResponse;
import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntryServiceImpl implements EntryService {

    private final RaftClusterInformation raftClusterInformation;
    private final EntryRepository entryRepository;

    EntryServiceImpl(RaftClusterInformation raftClusterInformation,
        EntryRepository entryRepository) {
        this.raftClusterInformation = raftClusterInformation;
        this.entryRepository = entryRepository;
    }

    @Override
    public AppendEntryResult appendEntries(EntryRequest entryRequest) {
        long timestamp = System.currentTimeMillis();
        var nodeState = raftClusterInformation.getNodeState();
        String leaderId = entryRequest.getLeaderId();
        long leaderTerm = entryRequest.getTerm();

        if(leaderTerm < nodeState.getTerm()) {
            return AppendEntryResult.builder()
                .success(false)
                .term(nodeState.getTerm())
                .build();
        }

        //another node in the same term wants to be leader, while im the leader. Reject it
        if(leaderTerm == nodeState.getTerm() && nodeState.getState() == NodeState.State.LEADER) {
            return AppendEntryResult.builder()
                .success(false)
                .term(nodeState.getTerm())
                .build();
        }

        nodeState.setLastHeardFromLeaderTimestampMilliseconds(timestamp);
        nodeState.setLeaderId(leaderId);
        nodeState.setTerm(leaderTerm);
        nodeState.setState(NodeState.State.FOLLOWER);
        log.info("Appending entries from leader {}", leaderId);
        return AppendEntryResult.builder()
            .success(true)
            .term(leaderTerm)
            .build();
    }

    @Override
    public void sendEntries() throws InterruptedException {
        var nodeState = raftClusterInformation.getNodeState();

        ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

        var ipOfNodes = raftClusterInformation.getIpOfNodes()
            .stream()
            .filter(ip -> !ip.equals(nodeState.getNodeName()))
            .collect(Collectors.toList());
        while (true) {
            nodeState.waitForStateChange();
            if(nodeState.getState() != NodeState.State.LEADER) continue;
            log.info("Im the leader");

            var shouldCancel = new Semaphore(0);
            var future = scheduler.scheduleAtFixedRate(() -> {
                    sendEntries(shouldCancel, nodeState, ipOfNodes);
                }, 0, SEND_ENTRIES_MILLISECOND_INTERVAL, TimeUnit.SECONDS);
            //block here until I lose leadership
            shouldCancel.acquire();
            future.cancel(true);
            scheduler.close();
            nodeState.setState(NodeState.State.FOLLOWER);
            log.info("Im follower now");
        }
    }

    private void sendEntries(Semaphore shouldCancelSending, NodeState nodeState, List<String> ipOfNodes) {
        long term = nodeState.getTerm();
        String leader = nodeState.getNodeName();
        if (nodeState.getState() != NodeState.State.LEADER) {
            shouldCancelSending.release();
            return;
        }
        EntryRequest entryRequest = EntryRequest.builder()
            .leaderId(leader)
            .term(term)
            .build();
        for (String nodeIp : ipOfNodes) {
            EntryResponse response = null;
            try {
                log.info("Sending entry to {}", nodeIp);
                response = entryRepository.sendEntry(nodeIp, entryRequest);
            } catch(Exception e) {
                //node is not responding
                log.error("Node {} not responding. Message={}", nodeIp, e.getMessage());
                continue;
            }
            if (!response.isSuccess()) {
                log.info("EntryResponse not success from node {}", nodeIp);
                if (term < response.getTerm()) {
                    shouldCancelSending.release();
                }
            }
        }
    }
}
