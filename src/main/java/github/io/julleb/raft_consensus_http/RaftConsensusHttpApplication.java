package github.io.julleb.raft_consensus_http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RaftConsensusHttpApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaftConsensusHttpApplication.class, args);
	}

}
