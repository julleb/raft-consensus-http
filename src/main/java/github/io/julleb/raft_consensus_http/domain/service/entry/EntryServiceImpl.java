package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import org.springframework.stereotype.Service;

@Service
public class EntryServiceImpl implements EntryService {


    @Override
    public AppendEntryResult appendEntries(EntryRequest entryRequest) {
        return null;
    }

    @Override
    public void sendEntries() {

    }
}
