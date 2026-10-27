package github.io.julleb.raft_consensus_http.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    public RestClient CreateRestClient() {
        return RestClient.create();
    }

    @Bean
    RestClient RestClientBean() {
        return CreateRestClient();
    }
}
