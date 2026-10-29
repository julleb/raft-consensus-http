package github.io.julleb.raft_consensus_http.domain.service.election;

import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;

public interface ElectionService {
    void candidateAsLeader();

    //returns the node's voted leader for specific term
    String getVotedLeaderForTerm(long term);

    ResponseVote handleRequestVote(RequestVote requestVote);
}
