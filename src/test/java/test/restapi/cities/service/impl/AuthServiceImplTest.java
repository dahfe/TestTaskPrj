package test.restapi.cities.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import test.restapi.cities.exception.ModelExistsException;
import test.restapi.cities.repository.RoleRepository;
import test.restapi.cities.repository.UserRepository;
import test.restapi.cities.security.JwtTokenUtil;
import test.restapi.cities.security.JwtUserDetailsService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AuthServiceImpl.class, BCryptPasswordEncoder.class})
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    private final String TEST_STRING = "test";

    @Test
    void loginWithUsernameAndPasswordTest() throws AuthenticationException {
        when(jwtUserDetailsService.loadUserByUsername(any()))
                .thenReturn(new User(TEST_STRING, TEST_STRING, new ArrayList<>()));
        when(jwtTokenUtil.generateToken(any())).thenReturn(TEST_STRING);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new TestingAuthenticationToken(TEST_STRING, TEST_STRING));
        assertEquals(TEST_STRING, authServiceImpl.loginWithUsernameAndPassword(TEST_STRING, TEST_STRING));
        verify(jwtUserDetailsService).loadUserByUsername(any());
        verify(jwtTokenUtil).generateToken(any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginWithUsernameAndPasswordShouldThrowDisabledException() throws AuthenticationException {
        lenient().when(jwtUserDetailsService.loadUserByUsername(any()))
                .thenReturn(new User(TEST_STRING, TEST_STRING, new ArrayList<>()));
        lenient().when(jwtTokenUtil.generateToken(any())).thenReturn(TEST_STRING);
        lenient().when(authenticationManager.authenticate(any())).thenThrow(new DisabledException("Msg"));
        assertThrows(DisabledException.class, () -> authServiceImpl.loginWithUsernameAndPassword(TEST_STRING, TEST_STRING));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginWithUsernameAndPasswordShouldThrowBadCredentialsException() throws AuthenticationException {
        lenient().when(jwtUserDetailsService.loadUserByUsername(any()))
                .thenReturn(new User(TEST_STRING, TEST_STRING, new ArrayList<>()));
        lenient().when(jwtTokenUtil.generateToken(any())).thenReturn(TEST_STRING);
        lenient().when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Msg"));
        assertThrows(BadCredentialsException.class,
                () -> authServiceImpl.loginWithUsernameAndPassword(TEST_STRING, TEST_STRING));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testSaveUserShouldThrowUsernameExistsException() {
        test.restapi.cities.model.User user = createUser();
        test.restapi.cities.model.User user1 = createUser();
        lenient().when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user1));
        test.restapi.cities.model.User user2 = createUser();
        assertThrows(ModelExistsException.class, () -> authServiceImpl.saveUser(user2));
        verify(userRepository).findByUsername(any());
    }

    @Test
    void testSaveUserShouldReturnCompareUserObjects() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(roleRepository.findByName(any())).thenReturn(new HashSet<>());

        test.restapi.cities.model.User user = createUser();
        authServiceImpl.saveUser(user);
        test.restapi.cities.model.User user1 = createUser();
        verify(userRepository).findByUsername(any());
        verify(roleRepository).findByName(any());
        assertEquals(TEST_STRING, user.getUsername());
        assertNotEquals(user.getPassword(), user1.getPassword());
        assertFalse(user.getRoles().isEmpty());
    }

    private test.restapi.cities.model.User createUser(){
        test.restapi.cities.model.User user = new test.restapi.cities.model.User();
        user.setId(1L);
        user.setPassword(TEST_STRING);
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        user.setUsername(TEST_STRING);
        return user;
    }
}

