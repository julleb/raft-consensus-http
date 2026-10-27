package github.io.julleb.raft_consensus_http.domain.service.heartbeat;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;

public interface HeartbeatService {
    boolean isLeaderOnline();
    NodeState getNodeState();
}
