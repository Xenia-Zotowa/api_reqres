package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class StatusTests {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
    @Test
    void getListUsersTests(){
        given()
                .log().all()
                .get("/users?page=2")
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
        String authData = "{\"name\": \"morpheus\", \"job\": \"leader\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().all()
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }
    @Test
    void putUpdateTests(){
        String authData = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().all()
                .when()
                .put("/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
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
