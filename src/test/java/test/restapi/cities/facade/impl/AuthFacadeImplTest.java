package test.restapi.cities.facade.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import test.restapi.cities.dto.authDto.AuthDto;
import test.restapi.cities.dto.entityDto.UserDto;
import test.restapi.cities.mapper.UserMapper;
import test.restapi.cities.model.User;
import test.restapi.cities.repository.RoleRepository;
import test.restapi.cities.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AuthFacadeImpl.class})
@ExtendWith(MockitoExtension.class)
class AuthFacadeImplTest {
    @InjectMocks
    private AuthFacadeImpl authFacadeImpl;

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    private final String TEST_STRING = "test";

    @Test
    void testGetTokenShouldReturnAccessToken() {
        when(authService.loginWithUsernameAndPassword(any(), any())).thenReturn(TEST_STRING);
        assertEquals(TEST_STRING, authFacadeImpl.getToken(new AuthDto(TEST_STRING, TEST_STRING)).getAccessToken());
        verify(authService).loginWithUsernameAndPassword(any(), any());
    }

    @Test
    void testSaveUser() {
        doNothing().when(authService).saveUser(any());
        User user = createUser();
        when(userMapper.toEntity(any())).thenReturn(user);
        authFacadeImpl.saveUser(new UserDto(TEST_STRING, TEST_STRING));
        verify(authService).saveUser(any());
        verify(userMapper).toEntity(any());
    }

    private User createUser(){
        test.restapi.cities.model.User user = new test.restapi.cities.model.User();
        user.setId(1L);
        user.setPassword(TEST_STRING);
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        user.setUsername(TEST_STRING);
        return user;
    }
}

