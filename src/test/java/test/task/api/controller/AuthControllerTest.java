package test.task.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.restassured.RestAssured;
import test.task.api.dto.authDto.AuthDto;
import test.task.api.dto.entityDto.UserDto;
import test.task.api.service.AuthService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AuthService authService;

    @Test
    public void testLoginWithNameAndPassword() {
        RestAssured.port = port;

        AuthDto authDto = new AuthDto();
        authDto.setUsername("testuser");
        authDto.setPassword("testpassword");

        given()
                .contentType("application/json")
                .body(authDto)
                .when()
                .post("/api/v1/auth")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue());
    }

    @Test
    public void testSaveUser() {
        RestAssured.port = port;

        UserDto userDto = new UserDto();
        userDto.setUsername("newuser");
        userDto.setPassword("newpassword");

        given()
                .contentType("application/json")
                .body(userDto)
                .when()
                .post("/api/v1/registering")
                .then()
                .statusCode(200);
    }
}