package github.io.julleb.raft_consensus_http.domain.service.heartbeat;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import org.springframework.stereotype.Service;

@Service
public class HearbeatServiceImpl implements HeartbeatService {
    private final RaftClusterInformation raftClusterInformation;
    private final HeartbeatRepository heartbeatRepository;

    public HearbeatServiceImpl(RaftClusterInformation raftClusterInformation,
        HeartbeatRepository heartbeatRepository) {
        this.raftClusterInformation = raftClusterInformation;
        this.heartbeatRepository = heartbeatRepository;
    }

    @Override
    public boolean isLeaderOnline() {

        String leaderIp = raftClusterInformation.getNodeState().getLeaderId();
        try {
            heartbeatRepository.checkHeartbeat(leaderIp);
        } catch(RuntimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public NodeState getNodeState() {
        return raftClusterInformation.getNodeState();
    }
}
