package github.io.julleb.raft_consensus_http.domain.model;

import java.util.concurrent.ThreadLocalRandom;


public class NodeState {
    private long term;
    //leaderId points to the nodeIp
    private String leaderId;
    private String nodeName;
    private State state = State.FOLLOWER;
    private long electionTimeoutMilliseconds
        = ThreadLocalRandom.current().nextLong(150, 300);
    private long lastHeardFromLeaderTimestampMilliseconds;


    public synchronized long getTerm() {
        return term;
    }

    public synchronized void setTerm(long term) {
        this.term = term;
    }

    public synchronized String getLeaderId() {
        return leaderId;
    }

    public synchronized void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public synchronized String getNodeName() {
        return nodeName;
    }

    public synchronized void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized void setState(State state) {
        this.state = state;
    }

    public synchronized long getElectionTimeoutMilliseconds() {
        return electionTimeoutMilliseconds;
    }

    public synchronized void setElectionTimeoutMilliseconds(long electionTimeoutMilliseconds) {
        this.electionTimeoutMilliseconds = electionTimeoutMilliseconds;
    }

    public synchronized long getLastHeardFromLeaderTimestampMilliseconds() {
        return lastHeardFromLeaderTimestampMilliseconds;
    }

    public synchronized void setLastHeardFromLeaderTimestampMilliseconds(long timestamp) {
        this.lastHeardFromLeaderTimestampMilliseconds = timestamp;
    }

    public enum State {
        FOLLOWER,
        LEADER,
        CANDIDATE,
    }
}
