package github.io.julleb.raft_consensus_http;

import github.io.julleb.raft_consensus_http.domain.service.election.ElectionService;
import github.io.julleb.raft_consensus_http.domain.service.entry.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class RaftConsensusHttpApplication {

    @Autowired
    private EntryService entryService;
    @Autowired
    private ElectionService electionService;

    public static void main(String[] args) {
        SpringApplication.run(RaftConsensusHttpApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStart() {
        new Thread(() -> {
            try {
                entryService.sendEntries();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "raft-background-task1").start();
    }

    @Scheduled(fixedDelay = 5000)
    public void candidateAsLeader(){
        electionService.candidateAsLeader();
    }

}
