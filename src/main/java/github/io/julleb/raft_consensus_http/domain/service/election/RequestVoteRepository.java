package github.io.julleb.raft_consensus_http.domain.service.election;

import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;

public interface RequestVoteRepository {
    ResponseVote request(String baseUrl, RequestVote requestVote);
}
