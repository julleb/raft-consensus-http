package github.io.julleb.raft_consensus_http.web.controller;

import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;
import github.io.julleb.raft_consensus_http.domain.service.election.ElectionService;
import github.io.julleb.raft_consensus_http.web.DTO.RequestVoteDto;
import github.io.julleb.raft_consensus_http.web.DTO.ResponseVoteDto;
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

    private final ElectionService electionService;

    VoteController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @Operation(summary = "Raft request vote")
    @PostMapping
    public ResponseEntity<ResponseVoteDto> requestVote(RequestVoteDto requestVoteDto) {

        RequestVote requestVote = RequestVote.builder()
            .term(requestVoteDto.getTerm())
            .candidateName(requestVoteDto.getCandidateId())
            .build();

        ResponseVote responseVote = electionService.handleRequestVote(requestVote);

        ResponseVoteDto responseDto = ResponseVoteDto.builder()
            .voteGranted(responseVote.isVoteGranted())
            .term(responseVote.getTerm())
            .build();
        return ResponseEntity.ok(responseDto);
    }

}
