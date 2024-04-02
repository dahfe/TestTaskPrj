package test.task.api.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import test.task.api.tool.RandomHashGenerator;
import test.task.api.dto.entityDto.TMARequestDto;
import test.task.api.exception.ModelExistsException;
import test.task.api.model.*;
import test.task.api.repository.ItemRepository;
import test.task.api.repository.TMARequestRepository;
import test.task.api.repository.UserRepository;
import test.task.api.service.TMARequestService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TMARequestServiceImpl implements TMARequestService {
    private final TMARequestRepository tmaRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    @Override
    public void createRequest(List<TMARequestDto> tmaRequestDtos) {

        User user = getCurrentUser();
        String randomHash = RandomHashGenerator.generateRandomHash();

        for (TMARequestDto tmaRequestDto : tmaRequestDtos){
            TMARequest tmaRequest  = new TMARequest();
            Item item = itemRepository.findByItemName(tmaRequestDto.getItemName()).orElseThrow();
            tmaRequest.setEmployee(user);
            tmaRequest.setItem(item);
            tmaRequest.setUnitOfMeasurement(item.getUnitOfMeasurement());
            tmaRequest.setQuantity(tmaRequestDto.getQuantity());
            tmaRequest.setPriceWithoutVAT(tmaRequestDto.getPriceWithoutVAT());
            tmaRequest.setStatus(Status.NEW.toString());
            tmaRequest.setComment(tmaRequestDto.getComment());
            tmaRequest.setRequestRowId(randomHash);
            tmaRequestRepository.save(tmaRequest);
        }
    }

    @Override
    public void changeRequestStatus(UUID requestId, String newStatus) {
        Optional<TMARequest> tmaRequestOptional = tmaRequestRepository.findById(requestId);
        if(tmaRequestOptional.isPresent()) {
            TMARequest tmaRequest = tmaRequestOptional.get();

            Item tmaRequestItem = tmaRequest.getItem();
            if (tmaRequestItem == null) {
                throw new ModelExistsException("Item does not exist");
            }
            Optional<Item> optionalItem = itemRepository.findById(tmaRequestItem.getId());
            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                if (item.getQuantity() < tmaRequest.getQuantity()) {
                    tmaRequest.setStatus(Status.REJECT_REQUEST.toString());
                    tmaRequestRepository.save(tmaRequest);
                    throw new ModelExistsException("Request error");
                }

                item.setQuantity(item.getQuantity() - tmaRequest.getQuantity());

                switch (newStatus) {
                    case "APPROVE":
                        tmaRequest.setStatus(Status.APPROVE.toString());
                        break;
                    case "REJECT_REQUEST":
                        tmaRequest.setStatus(Status.REJECT_REQUEST.toString());
                        break;
                }
                tmaRequestRepository.save(tmaRequest);
                itemRepository.save(item);
            }
        } else {
            throw new ModelExistsException("Item does not exist in the database");
        }
    }

    @Override
    public List<TMARequest> getTmaRequests(List<TMARequest> tmaRequests) {
        User user = getCurrentUser();

        Role role = user.getRoles().stream().findFirst().orElseThrow();
        if(role.getName().equals("ROLE_COORDINATOR") ||
                role.getName().equals("ROLE_ADMINISTRATOR")){
            return getAll();
        }
        List<TMARequest> sortedList = new ArrayList<>();
        for (TMARequest tmaRequest : tmaRequests){
            if(tmaRequest.getEmployee().getId() == user.getId()){
                sortedList.add(tmaRequest);
            }
        }
        return sortedList;
    }

    @Override
    public List<TMARequest> getAll() {
        return tmaRequestRepository.findAll();
    }

    @Override
    public List<TMARequest> filterTMARequests(String field, Object value, List<TMARequest> tmaRequests) {
        List<TMARequest> filteredRequests = tmaRequests.stream()
                .filter(tmaRequest -> {
                    switch (field){
                        case "employeeName": return tmaRequest.getEmployee().equals(value);
                        case "item": return tmaRequest.getItem().equals(value);
                        case "unitOfMeasurement": return tmaRequest.getUnitOfMeasurement().getUnit().equals(value.toString());
                        case "quantity": return tmaRequest.getQuantity() == (int) value;
                        case "priceWithoutVAT": return tmaRequest.getPriceWithoutVAT().equals(value);
                        case "status":
                            String status = tmaRequest.getStatus();
                            return status != null && status.equals(value.toString());
                        case "requestRowId": return tmaRequest.getRequestRowId().equals(value.toString());
                        default: return false;

                    }
                }).toList();
        return filteredRequests;
    }

    @Override
    public List<TMARequest> getAllTMARequestsSorted(String sortBy, List<TMARequest> tmaRequests) {
        return getTmaRequests(tmaRequestRepository.findAll(Sort.by(sortBy)).stream().toList());
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new ModelExistsException("User is not authenticated");
        }
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
    }
}
