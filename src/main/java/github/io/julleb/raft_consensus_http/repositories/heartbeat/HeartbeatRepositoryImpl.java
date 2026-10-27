package github.io.julleb.raft_consensus_http.repositories.heartbeat;

import github.io.julleb.raft_consensus_http.domain.service.heartbeat.HeartbeatRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class HeartbeatRepositoryImpl implements HeartbeatRepository {
    private final RestClient restClient;
    private final String heartBeatEndpoint = "/raft/heartbeat";

    HeartbeatRepositoryImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void checkHeartbeat(String baseUrl) {
        String url = baseUrl + heartBeatEndpoint;
        var isOk = restClient.get()
            .uri(url)
            .exchange((request, response) ->
                response.getStatusCode().value() == 200);
        if(Boolean.FALSE.equals(isOk)) throw new RuntimeException("not up");
    }
}
