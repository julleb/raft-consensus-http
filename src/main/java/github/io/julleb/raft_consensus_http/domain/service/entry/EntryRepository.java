package github.io.julleb.raft_consensus_http.domain.service.entry;

import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.model.EntryResponse;

public interface EntryRepository {
    EntryResponse sendEntry(String nodeIp, EntryRequest entryRequest);
}
