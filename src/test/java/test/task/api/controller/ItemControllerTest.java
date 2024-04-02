package test.task.api.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Test
    void getItems_NoFilterNoSort_ReturnsAllItems() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/items")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void getItems_FilterByField_ReturnsFilteredItems() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"field\":\"itemName\", \"value\":\"example\"}")
                .when()
                .post("/api/v1/items")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void getItems_SortByField_ReturnsSortedItems() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("sortBy", "itemName")
                .when()
                .post("/api/v1/items")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("collect{it.itemName}", contains("item1", "item2", "item3"));
    }

    @Test
    void createItem_ValidItem_ReturnsOk() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"itemName\":\"newItem\", \"quantity\":10, \"priceWithoutVAT\":100}")
                .when()
                .post("/api/v1/items/create")
                .then()
                .statusCode(200);
    }

    @Test
    void updateItem_ValidItem_ReturnsOk() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"itemName\":\"existingItem\", \"quantity\":20, \"priceWithoutVAT\":150}")
                .when()
                .post("/api/v1/items/existingItem/edit")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteItem_ExistingItem_ReturnsOk() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/items/existingItem/remove")
                .then()
                .statusCode(200);
    }
}
