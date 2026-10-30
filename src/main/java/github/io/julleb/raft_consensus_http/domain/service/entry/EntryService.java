package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.AppendEntryResult;
import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;

public interface EntryService {

    AppendEntryResult appendEntries(EntryRequest entryRequest);

    void sendEntries() throws InterruptedException;
}
