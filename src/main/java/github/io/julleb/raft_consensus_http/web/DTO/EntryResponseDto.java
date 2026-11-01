package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryResponseDto {
    private Long term;
    private boolean success;
}
