package github.io.julleb.raft_consensus_http.web.controller;

import github.io.julleb.raft_consensus_http.web.DTO.RequestVoteDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/raft/vote")
public class VoteController {

    @Operation(summary = "Raft request vote")
    @PostMapping
    public ResponseEntity<Void> requestVote(RequestVoteDto requestVoteDto) {
        return ResponseEntity.ok()
            .build();
    }

}
