package test.task.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import test.task.api.dto.authDto.AccessTokenDto;
import test.task.api.dto.authDto.AuthDto;
import test.task.api.dto.entityDto.UserDto;
import test.task.api.exception.ModelExistsException;
import test.task.api.model.User;
import test.task.api.repository.RoleRepository;
import test.task.api.repository.UserRepository;
import test.task.api.security.JwtTokenUtil;
import test.task.api.security.JwtUserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetToken() {
        AuthDto authDto = new AuthDto("username", "password");
        String token = "mockToken";
        UserDetails userDetails = mock(UserDetails.class);
        AccessTokenDto expectedAccessTokenDto = new AccessTokenDto(token);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUserDetailsService.loadUserByUsername(authDto.getUsername())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        AccessTokenDto actualAccessTokenDto = authService.getToken(authDto);

        assertEquals(expectedAccessTokenDto, actualAccessTokenDto);
    }

    @Test
    public void testSaveUser() {
        UserDto userDto = new UserDto("username", "password");
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(null);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> authService.saveUser(userDto));
    }

    @Test
    public void testSaveUser_UsernameAlreadyExists() {
        UserDto userDto = new UserDto("existingUsername", "password");

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(ModelExistsException.class, () -> authService.saveUser(userDto));
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        String username = "username";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException("USER_DISABLED"));

        assertThrows(DisabledException.class, () -> authService.authenticate(username, password));
    }

    @Test
    public void testAuthenticate_UserDisabled() {
        String username = "username";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException("USER_DISABLED"));

        assertThrows(DisabledException.class, () -> authService.authenticate(username, password));
    }
}
