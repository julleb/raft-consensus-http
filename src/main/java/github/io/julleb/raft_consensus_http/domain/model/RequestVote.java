package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestVote {
    private int term;
    private String candidateName;
    private String candidateData;
}
