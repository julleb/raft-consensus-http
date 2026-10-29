package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseVote {
    //responders current term
    private int term;
    private boolean voteGranted;
}
