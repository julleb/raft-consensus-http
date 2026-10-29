package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AppendEntryResult {
    private int term;
    private boolean success;
}
