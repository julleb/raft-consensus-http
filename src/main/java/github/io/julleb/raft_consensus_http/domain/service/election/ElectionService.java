package github.io.julleb.raft_consensus_http.domain.service.election;

public interface ElectionService {
    void electLeader();

    //returns the node's voted leader for specific term
    String getVotedLeaderForTerm(int term);
}
