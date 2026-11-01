package github.io.julleb.raft_consensus_http.domain.model;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "raft.cluster")
public class RaftClusterInformation {
    private List<String> ipOfNodes;
    private final NodeState nodeState = new NodeState();
    public static int SEND_ENTRIES_MILLISECOND_INTERVAL = 3000;
    public static int RANDOMNESS_IN_ELECTION_TIME_MIN = SEND_ENTRIES_MILLISECOND_INTERVAL + 500;
    public static int RANDOMNESS_IN_ELECTION_TIME_MAX = SEND_ENTRIES_MILLISECOND_INTERVAL + 1000;


    void setIpOfNodes(List<String> ipOfNodes) {
        this.ipOfNodes = ipOfNodes;
    }

    void setNodeName(String nodeName) {
        nodeState.setNodeName(nodeName);
    }
}
