package com.assignment.diff.jsoncomparison;

import static org.junit.Assert.assertEquals;

import static io.restassured.RestAssured.given;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.RestAssured;

/**
 * Integration tests for {@link JsonComparisonController}.
 *
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = JsonComparisonApplication.class,
    webEnvironment = WebEnvironment.RANDOM_PORT)
@Ignore
public class JsonComparisonApplicationIntegrationTest {
    @LocalServerPort
    private int port;

    private static DocumentContext testData;

    @BeforeClass
    public static void init() throws Exception {
        String data = new String(Files.readAllBytes(
            Paths.get(ClassLoader.getSystemResource("test_data.json").toURI())));
        testData = JsonPath.parse(data);
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testCase1DifferentJson() {
        String leftResponse = given()
            .body(testData.read("$.input.case_1.left.json").toString())
            .post(testData.read("$.input.case_1.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_1.left.response.status", "$.input.case_1.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_1.right.json").toString())
            .post(testData.read("$.input.case_1.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_1.right.response.status", "$.input.case_1.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_1.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_1.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_1.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_1.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
        assertEquals(testData.read("$.input.case_1.result.response.content.differences").toString(),
            jsonResponse.read("$.content.differences"));
    }

    @Test
    public void testCase2SameJson() {
        String leftResponse = given()
            .body(testData.read("$.input.case_2.left.json").toString())
            .post(testData.read("$.input.case_2.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_2.left.response.status", "$.input.case_2.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_2.right.json").toString())
            .post(testData.read("$.input.case_2.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_2.right.response.status", "$.input.case_2.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_2.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_2.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_2.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_2.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
    }

    @Test
    public void testCase3OnlyLeft() {
        String leftResponse = given()
            .body(testData.read("$.input.case_3_1.left.json").toString())
            .post(testData.read("$.input.case_3_1.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_3_1.left.response.status", "$.input.case_3_1.left.response.content",
            leftResponse);
        String response = given()
            .get(testData.read("$.input.case_3_1.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_3_1.result.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_3_1.result.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase3OnlyRight() {
        String rightResponse = given()
            .body(testData.read("$.input.case_3_2.right.json").toString())
            .post(testData.read("$.input.case_3_2.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_3_2.right.response.status", "$.input.case_3_2.right.response.content",
            rightResponse);
        String response = given()
            .get(testData.read("$.input.case_3_2.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_3_2.result.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_3_2.result.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase4DifferentSize() {
        String leftResponse = given()
            .body(testData.read("$.input.case_4.left.json").toString())
            .post(testData.read("$.input.case_4.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_4.left.response.status", "$.input.case_4.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_4.right.json").toString())
            .post(testData.read("$.input.case_4.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_4.right.response.status", "$.input.case_4.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_4.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_4.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_4.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_4.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
    }

    @Test
    public void testCase5NotFound() {
        String comparisonResult = given()
            .get(testData.read("$.input.case_5.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_5.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_5.result.response.content").toString(),
            jsonResponse.read("$.content"));

    }

    @Test
    public void testCase6CompletedComparisonUpdate() {
        String leftResponse = given()
            .body(testData.read("$.input.case_6.left.json").toString())
            .post(testData.read("$.input.case_6.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_6.left.response.status", "$.input.case_6.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_6.right.json").toString())
            .post(testData.read("$.input.case_6.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_6.right.response.status", "$.input.case_6.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_6.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_6.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_6.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_6.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
        assertEquals(testData.read("$.input.case_6.result.response.content.differences").toString(),
            jsonResponse.read("$.content.differences"));
        String rightReuploadResponse = given()
            .body(testData.read("$.input.case_6.right_reupload.json").toString())
            .post(testData.read("$.input.case_6.right_reupload.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext rightReuploadJson = JsonPath.parse(rightReuploadResponse);
        assertEquals(testData.read("$.input.case_6.right_reupload.response.status").toString(),
            rightReuploadJson.read("$.status").toString());
        assertEquals(testData.read("$.input.case_6.right_reupload.response.content").toString(),
            rightReuploadJson.read("$.content"));
    }

    private void assertBasicResponse(String keyStatus, String keyContent, String response) {
        DocumentContext ctx = JsonPath.parse(response);
        assertEquals(testData.read(keyStatus).toString(), ctx.read("$.status"));
        assertEquals(testData.read(keyContent).toString(), ctx.read("$.content"));
    }
}
