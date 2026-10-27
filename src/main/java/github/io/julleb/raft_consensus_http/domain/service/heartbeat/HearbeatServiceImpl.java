package github.io.julleb.raft_consensus_http.domain.service.heartbeat;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import org.springframework.stereotype.Service;

@Service
public class HearbeatServiceImpl implements HeartbeatService {

    private final NodeState nodeState;
    private final RaftClusterInformation raftClusterInformation;
    private final HeartbeatRepository heartbeatRepository;

    public HearbeatServiceImpl(NodeState nodeState,
        RaftClusterInformation raftClusterInformation,
        HeartbeatRepository heartbeatRepository) {
        this.nodeState = nodeState;
        this.raftClusterInformation = raftClusterInformation;
        this.heartbeatRepository = heartbeatRepository;
    }

    @Override
    public boolean isLeaderOnline() {

        String leaderIp = nodeState.getLeaderId();
        try {
            heartbeatRepository.checkHeartbeat(leaderIp);
        } catch(RuntimeException e) {
            return false;
        }
        return true;
    }
}
