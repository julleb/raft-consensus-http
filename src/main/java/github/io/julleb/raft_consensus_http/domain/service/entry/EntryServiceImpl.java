package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.model.EntryResponse;
import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
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

        long timestamp = System.currentTimeMillis();
        nodeState.setLastHeardFromLeaderTimestampMilliseconds(timestamp);
        nodeState.setLeaderId(leaderId);
        nodeState.setTerm(leaderTerm);
        nodeState.setState(NodeState.State.FOLLOWER);
        return AppendEntryResult.builder()
            .success(false)
            .term(leaderTerm)
            .build();
    }

    @Override
    public void sendEntries() throws InterruptedException {
        var nodeState = raftClusterInformation.getNodeState();

        ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
        ExecutorService executor =
            Executors.newFixedThreadPool(10);

        var ipOfNodes = raftClusterInformation.getIpOfNodes()
            .stream()
            .filter(ip -> ip.equals(nodeState.getNodeName()))
            .collect(Collectors.toSet());
        while(true) {
            nodeState.waitForLeadership();
            log.info("Im the leader");

            var shouldCancel = new Semaphore(0);
            var future = scheduler.scheduleAtFixedRate(() -> {
                long term = nodeState.getTerm();
                String leader = nodeState.getNodeName();
                if (nodeState.getState() != NodeState.State.LEADER) {
                    shouldCancel.release();
                    return;
                }
                EntryRequest entryRequest = EntryRequest.builder()
                    .leaderId(leader)
                    .term(term)
                    .build();
                for (String nodeIp : ipOfNodes) {
                    EntryResponse response = entryRepository.sendEntry(nodeIp, entryRequest);
                    if (!response.isSuccess()) {
                       if (term < response.getTerm()) {
                          shouldCancel.release();
                       }
                    }
                }

            }, 0, 3, TimeUnit.SECONDS);

            //block here until i lose leadership
            shouldCancel.acquire();
            future.cancel(true);
        }

    }
}
