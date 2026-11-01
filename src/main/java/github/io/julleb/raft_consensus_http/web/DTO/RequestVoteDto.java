package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class RequestVoteDto {
    private Long term;
    private String candidateId;
    private String candidateData;
}
