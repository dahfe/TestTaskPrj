package test.restapi.cities.restAssured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import test.restapi.cities.restAssured.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class CityTest {

    private final static String URL = "http://localhost:8081/api/v1";
    private final String TEST_STRING_AUTH_ADMIN = "testAdmin";
    private final String TEST_STRING_AUTH_USER = "test";
    private final String TEST_STRING_CITY = "nrekl.mg";
    private final Long TEST_LONG_CITY = 2L;
    private final Integer TEST_INT_CITY = 2;
    private TokenPojo accessToken;

    @Test
    public void testRegisterUserShouldReturnUser(){

        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(new AuthPojo(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER))
                    .post(URL + "/registering")
                .then()
                    .log().all()
                    .statusCode(200);
    }

    @Test
    public void testRegisterUserShouldReturnUserExistsException(){

        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(new AuthPojo(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER))
                    .post(URL + "/registering")
                .then()
                    .log().all()
                    .statusCode(400);
    }

    @Test
    public void testAuthUserShouldReturnAccessToken() {

        accessToken = RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(new AuthPojo(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER))
                    .post(URL + "/auth")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response().as(TokenPojo.class);
    }

    @Test
    public void testGetCityByNameShouldReturnCity(){

        String TEST_CITY_NAME = "test";

        RestAssured
                .given()
                .when()
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER).getAccessToken())
                    .get(URL + "/city/{name}", TEST_CITY_NAME)
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testGetCityByNameShouldReturnNotFoundException(){

        String TEST_CITY_NAME = "notFoundTest";

        RestAssured
                .given()
                .when()
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER).getAccessToken())
                    .get(URL + "/city/{name}", TEST_CITY_NAME)
                .then()
                    .log().all()
                    .statusCode(404)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testLoadCityShouldReturnLoadedCity(){

        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getCity())
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_ADMIN, TEST_STRING_AUTH_ADMIN).getAccessToken())
                    .post(URL + "/city/loading")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testLoadCityShouldReturnCityExistsException(){
        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getCity())
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_ADMIN, TEST_STRING_AUTH_ADMIN).getAccessToken())
                    .post(URL + "/city/loading")
                .then()
                    .log().all()
                    .statusCode(400)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testLoadCityShouldReturnNotAdminException(){

        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getCity())
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER).getAccessToken())
                    .post(URL + "/city/loading")
                .then()
                    .log().all()
                    .statusCode(403);
    }

    @Test
    public void testEditCityShouldReturnEditedCity(){

                RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getEditCity(TEST_LONG_CITY))
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_ADMIN, TEST_STRING_AUTH_ADMIN).getAccessToken())
                    .post(URL + "/city/editing")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testEditCityShouldReturnCityNotFoundException(){
        Long unusedId = 10000L;
        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getEditCity(unusedId))
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_ADMIN, TEST_STRING_AUTH_ADMIN).getAccessToken())
                    .post(URL + "/city/editing")
                .then()
                    .log().all()
                    .statusCode(404)
                    .extract().body().jsonPath().get("cityPojo");
    }

    @Test
    public void testEditCityShouldReturnNotAdminException(){

        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(getEditCity(TEST_LONG_CITY))
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER).getAccessToken())
                    .post(URL + "/city/editing")
                .then()
                    .log().all()
                    .statusCode(403);
    }

    @Test
    public void testGetCitiesShouldReturnCitiesPage(){

        RestAssured
                .given()
                    .log().all()
                    .queryParam("page", TEST_INT_CITY)
                    .queryParam("size", TEST_INT_CITY)
                .when()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + generatingAccessToken(TEST_STRING_AUTH_USER, TEST_STRING_AUTH_USER).getAccessToken())
                    .get(URL + "/getCities")
                .then()
                    .log().all()
                    .statusCode(200);
    }

    private TokenPojo generatingAccessToken(String username, String password){

        accessToken = RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(new AuthPojo(username, password))
                    .post(URL + "/auth")
                .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response().as(TokenPojo.class);

        return accessToken;
    }

    private CityPojo getCity() {
        return new CityPojo(TEST_STRING_CITY, getImageList());
    }

    private EditCityPojo getEditCity(Long id) {
        return new EditCityPojo(id, TEST_STRING_CITY, getImageList());
    }

    private  List<ImagePojo> getImageList(){
        List<ImagePojo> imageList = new ArrayList<>();
        imageList.add(new ImagePojo(TEST_STRING_CITY));
        return imageList;
    }
}
