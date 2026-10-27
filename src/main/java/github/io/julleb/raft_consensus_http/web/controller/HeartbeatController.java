package github.io.julleb.raft_consensus_http.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/raft/heartbeat")
public class HeartbeatController {

    @Operation(summary = "Raft Heartbeat")
    @GetMapping
    public ResponseEntity<Void> GetHeartbeat() {
        return ResponseEntity.ok().build();
    }

}
