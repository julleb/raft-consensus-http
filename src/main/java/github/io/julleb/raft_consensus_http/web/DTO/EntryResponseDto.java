package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
public class EntryResponseDto {
    private long term;
    private boolean success;
}
