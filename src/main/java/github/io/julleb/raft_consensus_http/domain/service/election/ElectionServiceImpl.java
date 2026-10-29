package github.io.julleb.raft_consensus_http.domain.service.election;

import github.io.julleb.raft_consensus_http.domain.model.NodeState;
import github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation;
import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;
import github.io.julleb.raft_consensus_http.domain.service.heartbeat.HeartbeatService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ElectionServiceImpl implements ElectionService {
    private final RaftClusterInformation raftClusterInformation;
    private final RequestVoteRepository requestVoteRepository;
    //maps a term to a node it has voted for as a leader
    private final Map<Long, String> termToVotedNode;
    private final int majority;

    public ElectionServiceImpl(RaftClusterInformation raftClusterInformation,
        RequestVoteRepository requestVoteRepository) {
        this.raftClusterInformation = raftClusterInformation;
        this.requestVoteRepository = requestVoteRepository;
        majority = (raftClusterInformation.getIpOfNodes().size() / 2) + 1;
        termToVotedNode = new ConcurrentHashMap<>();
    }

    @Override
    public void candidateAsLeader() {
        //increment term
        NodeState nodeState = raftClusterInformation.getNodeState();
        nodeState.setState(NodeState.State.CANDIDATE);
        int newTerm = nodeState.getTerm() + 1;
        nodeState.setTerm(newTerm);

        //vote for myself
        termToVotedNode.put(nodeState.getTerm(), nodeState.getNodeName());

        List<ResponseVote> responseVotes = sendVoteRequests(nodeState);
        long votes = responseVotes.stream().filter(ResponseVote::isVoteGranted).count();
        votes++; //calculate my own vote
        if(votes < majority) {
            log.debug("I don't have majority to be the leader");
            //before we set FOLLOWER, we need to check that we are a candidate
            nodeState.setState(NodeState.State.FOLLOWER);
            return;
        }
        nodeState.setState(NodeState.State.LEADER);
    }

    @Override
    public String getVotedLeaderForTerm(long term) {
        String leaderId = termToVotedNode.get(term);
        if(leaderId == null) return "";
        return leaderId;
    }

    @Override
    public ResponseVote handleRequestVote(RequestVote requestVote) {
        var nodeState = raftClusterInformation.getNodeState();
        int candidatesTerm = requestVote.getTerm();
        String candidateId = requestVote.getCandidateName();
        if(nodeState.getTerm() > candidatesTerm) {
            //candidate's term cannot be older than my current term
            return ResponseVote.builder().voteGranted(false)
                .term(nodeState.getTerm())
                .build();
        }
        String votedLeaderForThisTerm = getVotedLeaderForTerm(candidatesTerm);
        if(!votedLeaderForThisTerm.equals(requestVote.getCandidateName())) {
            //i have already voted for someone else
            return ResponseVote.builder().voteGranted(false)
                .term(nodeState.getTerm())
                .build();
        }

        //accept the candidate on this term
        termToVotedNode.put(candidatesTerm, candidateId);
        return ResponseVote.builder()
            .voteGranted(true)
            .term(candidatesTerm)
            .build();
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
