package github.io.julleb.raft_consensus_http.domain.service.election;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import github.io.julleb.raft_consensus_http.domain.service.heartbeat.HeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ElectionServiceImpl implements ElectionService {
    private final RaftClusterInformation raftClusterInformation;
    private final HeartbeatService heartbeatService;

    public ElectionServiceImpl(RaftClusterInformation raftClusterInformation,
        HeartbeatService heartbeatService) {
        this.raftClusterInformation = raftClusterInformation;
        this.heartbeatService = heartbeatService;
    }

    @Override
    public void electLeader() {
        if(isLeaderUp()) return;
        NodeState nodeState = raftClusterInformation.getNodeState();
        int newTerm = nodeState.getTerm() + 1;
        nodeState.setTerm(newTerm);
    }

    private boolean isLeaderUp() {
        String leaderId = raftClusterInformation.getNodeState().getLeaderId();
        if(!StringUtils.hasText(leaderId)) return false;
        return heartbeatService.isLeaderOnline();
    }
}
