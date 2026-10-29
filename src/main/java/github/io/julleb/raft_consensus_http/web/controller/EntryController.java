package github.io.julleb.raft_consensus_http.web.controller;

import github.io.julleb.raft_consensus_http.domain.service.entry.EntryService;
import github.io.julleb.raft_consensus_http.web.DTO.HeartbeatDto;
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
    public ResponseEntity<HeartbeatDto> AppendEntry() {
        entryService.appendEntries();
        return ResponseEntity.ok(null);
    }
}
