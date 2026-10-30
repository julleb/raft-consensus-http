package github.io.julleb.raft_consensus_http.web.controller;

import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.service.entry.EntryService;
import github.io.julleb.raft_consensus_http.web.DTO.EntryRequestDto;
import github.io.julleb.raft_consensus_http.web.DTO.EntryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/raft/entry")
public class EntryController {

    private final EntryService entryService;

    EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @Operation(summary = "append entries")
    @PostMapping(value = "append")
    public ResponseEntity<EntryResponseDto> AppendEntry(EntryRequestDto entryRequestDto) {

        var entryReq = EntryRequest.builder()
            .term(entryRequestDto.getTerm())
            .leaderId(entryRequestDto.getLeaderId())
            .build();

        var result = entryService.appendEntries(entryReq);
        EntryResponseDto responseDto = EntryResponseDto.builder()
            .success(result.isSuccess())
            .term(result.getTerm())
            .build();
        return ResponseEntity.ok(responseDto);
    }
}
