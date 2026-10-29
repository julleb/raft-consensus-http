package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
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
        return null;
    }

    @Override
    public void sendEntries() {

    }
}
