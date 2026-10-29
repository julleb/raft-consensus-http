package github.io.julleb.raft_consensus_http.domain.model;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeState {
    //TODO it should be locks here.
    private long term;
    //leaderId points to the nodeIp
    private String leaderId;
    private String currentData;
    private String nodeName;
    private State state = State.FOLLOWER;
    private long electionTimeoutMilliseconds
        = ThreadLocalRandom.current().nextLong(150, 300);
    private long lastHeardFromLeaderTimestampMilliseconds;

    public enum State {
        FOLLOWER,
        LEADER,
        CANDIDATE,
    }
}
