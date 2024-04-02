package test.task.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import test.task.api.dto.entityDto.TMARequestDto;
import test.task.api.model.Item;
import test.task.api.model.Role;
import test.task.api.model.TMARequest;
import test.task.api.model.User;
import test.task.api.repository.ItemRepository;
import test.task.api.repository.TMARequestRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TMARequestServiceImplTest {

    @Mock
    TMARequestRepository tmaRequestRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    TMARequestServiceImpl tmaRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void createRequest_Success() {
        List<TMARequestDto> tmaRequestDtos = new ArrayList<>();
        TMARequestDto tmaRequestDto = new TMARequestDto();
        tmaRequestDtos.add(tmaRequestDto);
        when(itemRepository.findByItemName(anyString())).thenReturn(Optional.of(new Item()));

        tmaRequestService.createRequest(tmaRequestDtos);

        verify(tmaRequestRepository, times(tmaRequestDtos.size())).save(any(TMARequest.class));
    }

    @Test
    void changeRequestStatus_Success() {
        UUID requestId = UUID.randomUUID();
        String newStatus = "APPROVE";
        TMARequest tmaRequest = new TMARequest();
        when(tmaRequestRepository.findById(requestId)).thenReturn(Optional.of(tmaRequest));
        Item item = new Item();
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        tmaRequestService.changeRequestStatus(requestId, newStatus);

        assertEquals(newStatus, tmaRequest.getStatus());
        verify(tmaRequestRepository, times(1)).save(tmaRequest);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void getTmaRequests_ReturnsCorrectList_ForAdministratorRole() {
        User administrator = new User();
        Role administratorRole = new Role();
        administratorRole.setName("ROLE_ADMINISTRATOR");
        administrator.setRoles(Set.of(administratorRole));
        List<TMARequest> tmaRequests = Arrays.asList(new TMARequest(), new TMARequest());
        when(tmaRequestRepository.findAll()).thenReturn(tmaRequests);

        List<TMARequest> result = tmaRequestService.getTmaRequests(new ArrayList<>());

        assertEquals(tmaRequests.size(), result.size());
        assertEquals(tmaRequests, result);
    }


    @Test
    void getAllTMARequestsSorted_ReturnsSortedRequests() {
        List<TMARequest> tmaRequests = tmaRequestList();
        when(tmaRequestRepository.findAll(Sort.by(Sort.Direction.ASC, "employeeName"))).thenReturn(tmaRequests);

        List<TMARequest> result = tmaRequestService.getAllTMARequestsSorted("employeeName", new ArrayList<>());

        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getEmployee().getUsername());
        assertEquals("Bob", result.get(1).getEmployee().getUsername());
        assertEquals("John", result.get(2).getEmployee().getUsername());
    }

    private TMARequest createTMARequest(String employeeName, String itemName) {
        User user = new User();
        user.setUsername(employeeName);
        TMARequest tmaRequest = new TMARequest();
        tmaRequest.setEmployee(user);
        Item item = new Item();
        item.setItemName(itemName);
        tmaRequest.setItem(item);
        return tmaRequest;
    }

    @Test
    void getTmaRequests_ReturnsCorrectList_ForCoordinatorRole() {
        User coordinator = new User();
        Role coordinatorRole = new Role();
        coordinatorRole.setName("ROLE_COORDINATOR");
        coordinator.setRoles(Set.of(coordinatorRole));
        List<TMARequest> allTmaRequests = tmaRequestList();

        when(tmaRequestRepository.findAll()).thenReturn(allTmaRequests);
        List<TMARequest> result = tmaRequestService.getTmaRequests(new ArrayList<>());

        assertEquals(0, result.size());
    }

    @Test
    void filterTMARequests_ReturnsEmptyList_WhenNoMatchingField() {
        List<TMARequest> tmaRequests = tmaRequestList();

        List<TMARequest> result = tmaRequestService.filterTMARequests("status", "APPROVE", tmaRequests);

        assertEquals(0, result.size());
    }

    @Test
    void getAllTMARequestsSorted_ReturnsEmptyList_WhenRepositoryReturnsEmptyList() {
        when(tmaRequestRepository.findAll(Sort.by(Sort.Direction.ASC, "employeeName"))).thenReturn(Collections.emptyList());

        List<TMARequest> result = tmaRequestService.getAllTMARequestsSorted("employeeName", new ArrayList<>());

        assertEquals(0, result.size());
    }

    @Test
    void getAllTMARequestsSorted_ReturnsSortedRequests_WhenRepositoryReturnsNonEmptyList() {
        List<TMARequest> tmaRequests = tmaRequestList();
        when(tmaRequestRepository.findAll(Sort.by(Sort.Direction.ASC, "employeeName"))).thenReturn(tmaRequests);

        List<TMARequest> result = tmaRequestService.getAllTMARequestsSorted("employeeName", new ArrayList<>());

        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getEmployee().getUsername());
        assertEquals("Bob", result.get(1).getEmployee().getUsername());
        assertEquals("John", result.get(2).getEmployee().getUsername());
    }

    private List<TMARequest> tmaRequestList(){
        return Arrays.asList(
                createTMARequest("John", "item1"),
                createTMARequest("Alice", "item2"),
                createTMARequest("Bob", "item3")
        );
    }

}

