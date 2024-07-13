package tests;

import io.restassured.RestAssured;
import models.BodyModel;
import models.ResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StatusTestsModel {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
    @Test
    void getListUsersTests(){
        given()
                .log().all()
                .queryParam("page",2)
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", notNullValue())
                .body("data.id", hasItems(7))
                .body("data.email", hasItems("lindsay.ferguson@reqres.in"));
    }
    @Test
    void getSingleUserTests(){
        given()
                .log().all()
                .get("/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", is(2))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
    @Test
    void getSingleUserNotFound(){
        given()
                .log().all()
                .get("/users/23")
                .then()
                .log().all()
                .statusCode(404);
    }
    @Test
    void postCreateTest(){
        BodyModel authData = new BodyModel();
        authData.setName("morpheus");
        authData.setJob("leader");
        ResponseModel response = given()
                .body(authData)
                .contentType(JSON)
                .log().all()
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(ResponseModel.class);
        assertEquals("morpheus", response.getName());
        assertEquals("leader", response.getJob());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getId());
    }
    @Test
    void putUpdateTests(){
        BodyModel authData = new BodyModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");
        ResponseModel response = given()
                .body(authData)
                .contentType(JSON)
                .log().all()
                .when()
                .put("/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(ResponseModel.class);
        assertEquals("morpheus", response.getName());
        assertEquals("zion resident", response.getJob());
        assertNotNull(response.getCreatedAt());
    }
    @Test
    void deleteTests(){
        given()
                .log().all()
                .delete("/users/2")
                .then()
                .log().all()
                .statusCode(204);
    }
}
