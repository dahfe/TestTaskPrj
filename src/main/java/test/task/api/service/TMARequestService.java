package test.task.api.service;

import test.task.api.dto.entityDto.TMARequestDto;
import test.task.api.model.TMARequest;

import java.util.List;
import java.util.UUID;

public interface TMARequestService {
    void createRequest(List<TMARequestDto> tmaRequestDtos);
    void changeRequestStatus(UUID requestId, String newStatus);

    List<TMARequest> getTmaRequests(List<TMARequest> tmaRequests);
    List<TMARequest> getAll();
    List<TMARequest> filterTMARequests(String field, Object value, List<TMARequest> tmaRequests);
    List<TMARequest> getAllTMARequestsSorted(String sortBy, List<TMARequest> tmaRequests);
}
