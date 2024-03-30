package test.task.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.task.api.dto.authDto.AccessTokenDto;
import test.task.api.dto.authDto.AuthDto;
import test.task.api.dto.entityDto.UserDto;
import test.task.api.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<AccessTokenDto> loginWithNameAndPassword(@RequestBody @Valid AuthDto authDto) {
        return new ResponseEntity<>(authService.getToken(authDto), HttpStatus.OK);
    }

    @PostMapping("/registering")
    public ResponseEntity saveUser(@RequestBody @Valid UserDto userDto) {
        authService.saveUser(userDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
