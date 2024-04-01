package test.task.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.task.api.dto.entityDto.FilterDto;
import test.task.api.dto.entityDto.TMARequestDto;
import test.task.api.model.TMARequest;
import test.task.api.service.TMARequestService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TMARequestController {
    private final TMARequestService tmaRequestService;

    @PostMapping("/requests/create")
    public ResponseEntity createTMARequest(@RequestBody @Valid List<TMARequestDto> tmaRequestDtos) throws IOException {
        tmaRequestService.createRequest(tmaRequestDtos);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/requests/{requestId}/status")
    public ResponseEntity updateProductStatus(@PathVariable String requestId, @RequestParam String newStatus) {
        tmaRequestService.changeRequestStatus(UUID.fromString(requestId), newStatus);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<List<TMARequest>> getTMARequests(@RequestBody(required = false) @Valid FilterDto filterDto,
                                                     @RequestParam(required = false) String sortBy) {
        List<TMARequest> tmaRequests = tmaRequestService.getTmaRequests(tmaRequestService.getAll());
        if (sortBy != null) {
            tmaRequests = tmaRequestService.getAllTMARequestsSorted(sortBy, tmaRequests);
        }
        if(filterDto == null){
            return new ResponseEntity<>(tmaRequests, HttpStatus.OK);
        }
        return new ResponseEntity<>(tmaRequestService.filterTMARequests(filterDto.getField(), filterDto.getValue(), tmaRequests), HttpStatus.OK);
    }
}
