package com.icubedm.n26_test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.icubedm.n26_test.Application;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jayway.restassured.RestAssured.given;

public class BasicIntegrationTest {

    private String POST_PATH = "http://localhost:8080/transactions";
    private String GET_PATH = "http://localhost:8080/statistics";
    private Gson GSON = new Gson();

    @Before
    public void setUp() throws Exception {
        Application.main(new String[]{}); // run it as standalone app to not cause context sharing between tests
    }



    @Test
    public void testName() throws Exception {

        long now = DateTimeUtil.nowEpochMilli();

        Transaction tx = new Transaction(200.0, now);

        String response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(GSON.toJson(tx))
                .post(POST_PATH)
                .then()
                .statusCode(201)
                .extract().body().toString();

        System.out.println(response);
    }
}
