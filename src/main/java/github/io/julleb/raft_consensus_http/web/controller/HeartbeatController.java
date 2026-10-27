package github.io.julleb.raft_consensus_http.web.controller;

import github.io.julleb.raft_consensus_http.domain.service.heartbeat.HeartbeatService;
import github.io.julleb.raft_consensus_http.web.DTO.HeartbeatDto;
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

    private final HeartbeatService heartbeatService;

    HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @Operation(summary = "Raft Heartbeat")
    @GetMapping
    public ResponseEntity<HeartbeatDto> GetHeartbeat() {
        var nodeState = heartbeatService.getNodeState();

        var heartBeatDto = HeartbeatDto.builder()
            .state(nodeState.getState().toString())
            .term(nodeState.getTerm())
            .nodeName(nodeState.getNodeName())
            .build();
        return ResponseEntity.ok(heartBeatDto);
    }

}
