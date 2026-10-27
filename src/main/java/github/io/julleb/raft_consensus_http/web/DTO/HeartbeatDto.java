package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HeartbeatDto {
    private int term;
    private String nodeName;
    private String state;
}
