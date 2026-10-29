package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseVoteDto {
    private long term;
    private boolean voteGranted;
}
