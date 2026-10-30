package github.io.julleb.raft_consensus_http.repository.vote;

import github.io.julleb.raft_consensus_http.domain.model.RequestVote;
import github.io.julleb.raft_consensus_http.domain.model.ResponseVote;
import github.io.julleb.raft_consensus_http.domain.service.election.RequestVoteRepository;
import github.io.julleb.raft_consensus_http.web.DTO.RequestVoteDto;
import github.io.julleb.raft_consensus_http.web.DTO.ResponseVoteDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class RequestVoteRepositoryImpl implements RequestVoteRepository {

    private final String endpointUrl = "/raft/vote";
    private final RestClient restClient;

    RequestVoteRepositoryImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ResponseVote request(String baseUrl, RequestVote requestVote) {
        String url = baseUrl + endpointUrl;

        RequestVoteDto dto = RequestVoteDto.builder()
            .candidateId(requestVote.getCandidateName())
            .term(requestVote.getTerm())
            .build();

        var responseVoteDto = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .retrieve()
            .body(ResponseVoteDto.class);

        if(responseVoteDto == null) throw new RuntimeException("response is empty");
        return ResponseVote.builder()
            .voteGranted(responseVoteDto.isVoteGranted())
            .term(responseVoteDto.getTerm())
            .build();
    }
}
