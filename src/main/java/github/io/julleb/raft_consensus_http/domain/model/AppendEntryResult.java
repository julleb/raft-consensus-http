package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
public class AppendEntryResult {
    private long term;
    private boolean success;
}
