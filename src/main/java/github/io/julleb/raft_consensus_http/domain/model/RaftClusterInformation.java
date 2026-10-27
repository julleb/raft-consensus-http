package github.io.julleb.raft_consensus_http.domain.model;

import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "raft.cluster")
public class RaftClusterInformation {

    private String nodeName;
    private List<String> ipOfNodes;

    void setIpOfNodes(List<String> ipOfNodes) {
        this.ipOfNodes = ipOfNodes;
    }
}
