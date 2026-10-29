package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntryRequestDto {
    private long term;
    private String leaderId;
}
