package github.io.julleb.raft_consensus_http.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class RequestVote {
    private final long term;
    private final String candidateName;

    @Builder
    public RequestVote(long term, String candidateName) {
        if(term < 0) throw new IllegalArgumentException("term is null");
        if(!StringUtils.hasText(candidateName)) throw new IllegalArgumentException("candidateName must contains text");
        this.term = term;
        this.candidateName = candidateName;
    }
}
