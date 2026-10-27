package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeState {
    //TODO, maybe it should be locks here.
    private int term;

    //leaderId points to the nodeIp
    private String leaderId;
    private String currentData;
}
