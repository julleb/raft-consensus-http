package github.io.julleb.raft_consensus_http.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryRequestDto {
    private Long term;
    private String leaderId;
}
