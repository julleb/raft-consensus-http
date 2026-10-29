package github.io.julleb.raft_consensus_http.repository.entry;

import github.io.julleb.raft_consensus_http.domain.model.EntryRequest;
import github.io.julleb.raft_consensus_http.domain.model.EntryResponse;
import github.io.julleb.raft_consensus_http.domain.service.entry.EntryRepository;
import github.io.julleb.raft_consensus_http.web.DTO.EntryRequestDto;
import github.io.julleb.raft_consensus_http.web.DTO.EntryResponseDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

@Repository
public class EntryRepositoryImpl implements EntryRepository {

    private final RestClient restClient;
    private final String endpointUrl = "/raft/entry/append";

    EntryRepositoryImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public EntryResponse sendEntry(String nodeIp, EntryRequest entryRequest) {
        String url = nodeIp + endpointUrl;
        var entryRequestDto = new EntryRequestDto();

        var responseDto = restClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(entryRequest)
            .retrieve()
            .body(EntryResponseDto.class);
        return new EntryResponse();
    }
}
