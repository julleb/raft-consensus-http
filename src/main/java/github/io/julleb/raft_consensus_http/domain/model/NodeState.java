package github.io.julleb.raft_consensus_http.domain.model;

import static github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation.RANDOMNESS_IN_ELECTION_TIME_MAX;
import static github.io.julleb.raft_consensus_http.domain.model.RaftClusterInformation.RANDOMNESS_IN_ELECTION_TIME_MIN;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;


public class NodeState {
    private long term = 0;
    //leaderId points to the nodeIp
    private String leaderId;
    private String nodeName;
    private State state = State.FOLLOWER;
    private long electionTimeoutMilliseconds
        = ThreadLocalRandom.current().nextLong(RANDOMNESS_IN_ELECTION_TIME_MIN, RANDOMNESS_IN_ELECTION_TIME_MAX);
    private long lastHeardFromLeaderTimestampMilliseconds;
    private final Semaphore stateChanged = new Semaphore(0);

    public void waitForStateChange() throws InterruptedException {
        stateChanged.acquire();
    }

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
        stateChanged.release();
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
