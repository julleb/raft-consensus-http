package github.io.julleb.raft_consensus_http.domain.service.election;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;
import github.io.julleb.raft_consensus_http.domain.service.heartbeat.HeartbeatService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ElectionServiceImpl implements ElectionService {
    private final RaftClusterInformation raftClusterInformation;
    private final HeartbeatService heartbeatService;
    private final RequestVoteRepository requestVoteRepository;

    //maps a term to a node it has voted for as a leader
    private Map<Integer, String> termToVotedNode;

    private final int majority;

    public ElectionServiceImpl(RaftClusterInformation raftClusterInformation,
        HeartbeatService heartbeatService,
        RequestVoteRepository requestVoteRepository) {
        this.raftClusterInformation = raftClusterInformation;
        this.heartbeatService = heartbeatService;
        this.requestVoteRepository = requestVoteRepository;
        majority = (raftClusterInformation.getIpOfNodes().size() / 2) + 1;
    }

    @Override
    public void electLeader() {
        if(isLeaderUp()) return;

        //increment term
        NodeState nodeState = raftClusterInformation.getNodeState();
        int newTerm = nodeState.getTerm() + 1;
        nodeState.setTerm(newTerm);

        //vote for myself
        termToVotedNode.put(nodeState.getTerm(), nodeState.getNodeName());

        List<ResponseVote> responseVotes = sendVoteRequests(nodeState);
        long votes = responseVotes.stream().filter(ResponseVote::isVoteGranted).count();
        votes++; //calculate my own vote
        if(votes < majority) {
            log.debug("I don't have majority to be the leader");
            return;
        }

        //send im the leader to everyone
    }

    private boolean isLeaderUp() {
        String leaderId = raftClusterInformation.getNodeState().getLeaderId();
        if(!StringUtils.hasText(leaderId)) return false;
        return heartbeatService.isLeaderOnline();
    }

    private List<ResponseVote> sendVoteRequests(NodeState nodeState) {
        RequestVote requestVote = RequestVote.builder()
            .term(nodeState.getTerm())
            .candidateName(nodeState.getNodeName())
            .build();
        List<ResponseVote> responseVotes = new ArrayList<>();
        for(String nodeIp : raftClusterInformation.getIpOfNodes()) {
            if(nodeIp.equals(nodeState.getNodeName())) continue; //dont send to yourself
            ResponseVote responseVote = requestVoteRepository.request(nodeIp, requestVote);
            responseVotes.add(responseVote);
        }
        return responseVotes;
    }
}
