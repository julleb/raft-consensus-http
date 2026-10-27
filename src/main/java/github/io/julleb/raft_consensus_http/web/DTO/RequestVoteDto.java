package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestVoteDto {
    private long term;
    private String candidateId;
    private String candidateData;
}
