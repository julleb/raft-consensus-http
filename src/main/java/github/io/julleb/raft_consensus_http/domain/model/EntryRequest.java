package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntryRequest {
    private int term;
    private String leaderId;
}
