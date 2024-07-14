package tests;

import io.restassured.RestAssured;
import models.BodyModel;
import models.ResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Spec.*;

public class StatusTestsModel {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
    @Test
    void getListUsersTests(){
        step("We send a Get request to List Users and check the response", ()-> given(getDeleteRequestSpec)
                .queryParam("page",2)
                .get("/users")
                .then()
                .spec(getPutResponseSpec)
                .body("page", notNullValue())
                .body("data.id", hasItems(7))
                .body("data.email", hasItems("lindsay.ferguson@reqres.in")));
    }
    @Test
    void getSingleUserTests(){
        step("We send a Get request for Single Users and check the response", ()-> given(getDeleteRequestSpec)
                .get("/users/2")
                .then()
                .spec(getPutResponseSpec)
                .body("data.id", is(2))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!")));
    }
    @Test
    void getSingleUserNotFoundTests(){
        step("We send a Get request for Single Users Not Found and check the response", ()-> given(getDeleteRequestSpec)
                .get("/users/23")
                .then()
                .spec(getmissingResponseSpec));
    }
    @Test
    void postCreateTests(){
        BodyModel authData = new BodyModel();
        authData.setName("morpheus");
        authData.setJob("leader");
        ResponseModel response =  step("Sending a POST request for Create", ()-> given(postPutRequestSpec)
                            .body(authData)
                            .when()
                            .post("/users")
                            .then()
                            .spec(postResponseSpec)
                            .extract().as(ResponseModel.class));
        step("Checking the response for the Create POST request", ()->{
        assertEquals("morpheus", response.getName());
        assertEquals("leader", response.getJob());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getId());
        });
    }
    @Test
    void putUpdateTests(){
        BodyModel authData = new BodyModel();
        authData.setName("morpheus");
        authData.setJob("zion resident");
        ResponseModel response = step("Sending a PUT request for an Update", ()-> given(postPutRequestSpec)
                .body(authData)
                .when()
                .put("/users/2")
                .then()
                .spec(getPutResponseSpec)
                .extract().as(ResponseModel.class));
        step("Checking the response for the PUT Update request", ()->{
        assertEquals("morpheus", response.getName());
        assertEquals("zion resident", response.getJob());
        assertNotNull(response.getUpdatedAt());
        });
    }
    @Test
    void deleteTests(){
        step("We send a DELETE request and check the response", ()-> given(getDeleteRequestSpec)
                .delete("/users/2")
                .then()
                .spec(deleteResponseSpec));
    }
}
