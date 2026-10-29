package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import org.springframework.stereotype.Service;

@Service
public class EntryServiceImpl implements EntryService {

    private final RaftClusterInformation raftClusterInformation;

    EntryServiceImpl(RaftClusterInformation raftClusterInformation) {
        this.raftClusterInformation = raftClusterInformation;
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

        //another node in the same term wants to be leader, reject it
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
    public void sendEntries() {

    }
}
