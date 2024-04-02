package test.task.api.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TMARequestControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void testCreateTMARequest() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("{ \"requestId\": \"123\", \"newStatus\": \"APPROVE\" }")
                .when()
                .post("/api/v1/requests/create")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testUpdateProductStatus() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("[{ \"name\": \"itemName\", \"quantity\": 5 }]")
                .when()
                .post("/api/v1/requests/123/status")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGetTMARequests() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .param("sortBy", "fieldName")
                .when()
                .post("/api/v1/requests")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(1));
    }
}
