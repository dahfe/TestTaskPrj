package test.task.api.service.Impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.task.api.dto.authDto.AccessTokenDto;
import test.task.api.dto.authDto.AuthDto;
import test.task.api.dto.entityDto.UserDto;
import test.task.api.exception.ModelExistsException;
import test.task.api.model.User;
import test.task.api.repository.RoleRepository;
import test.task.api.repository.UserRepository;
import test.task.api.security.JwtTokenUtil;
import test.task.api.security.JwtUserDetailsService;
import test.task.api.service.AuthService;


@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AccessTokenDto getToken(AuthDto authDto){
        return new AccessTokenDto(loginWithUsernameAndPassword(authDto.getUsername(), authDto.getPassword()));
    }

    private String loginWithUsernameAndPassword(String username, String password){
        authenticate(username, password);
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);

    }

    private void authenticate(String username, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public void saveUser(UserDto userDto) {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new ModelExistsException("Username already taken");
        }
        User user = new User();
        user.setRoles(roleRepository.findByName("EMPLOYEE"));
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }
}
