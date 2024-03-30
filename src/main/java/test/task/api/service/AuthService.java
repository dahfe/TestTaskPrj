package test.task.api.service;

import test.task.api.dto.authDto.AccessTokenDto;
import test.task.api.dto.authDto.AuthDto;
import test.task.api.dto.entityDto.UserDto;

public interface AuthService {
    AccessTokenDto getToken(AuthDto authDto);
    void saveUser (UserDto userDto);
}
