package github.io.julleb.raft_consensus_http.domain.service.heartbeat;

public interface HeartbeatRepository {
    void checkHeartbeat(String baseUrl);
}
