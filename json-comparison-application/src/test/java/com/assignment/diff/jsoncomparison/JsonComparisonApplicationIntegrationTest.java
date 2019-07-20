package com.assignment.diff.jsoncomparison;

import static org.junit.Assert.assertEquals;

import static io.restassured.RestAssured.given;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.Before;
import org.junit.BeforeClass;
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
 * For integration tests I am using akin proto Data Driven Testing.
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
    public void testCase2InvalidLeftJson() {
        String response = given()
            .body(testData.read("$.input.case_2_1.left.json").toString())
            .post(testData.read("$.input.case_2_1.left.url").toString())
            .then()
            .statusCode(400)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_2_1.left.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_2_1.left.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase2InvalidRightJson() {
        String response = given()
            .body(testData.read("$.input.case_2_2.right.json").toString())
            .post(testData.read("$.input.case_2_2.right.url").toString())
            .then()
            .statusCode(400)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_2_2.right.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_2_2.right.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase3SameJson() {
        String leftResponse = given()
            .body(testData.read("$.input.case_3.left.json").toString())
            .post(testData.read("$.input.case_3.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_3.left.response.status", "$.input.case_3.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_3.right.json").toString())
            .post(testData.read("$.input.case_3.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_3.right.response.status", "$.input.case_3.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_3.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_3.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_3.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_3.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
    }

    @Test
    public void testCase4OnlyLeft() {
        String leftResponse = given()
            .body(testData.read("$.input.case_4_1.left.json").toString())
            .post(testData.read("$.input.case_4_1.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_4_1.left.response.status", "$.input.case_4_1.left.response.content",
            leftResponse);
        String response = given()
            .get(testData.read("$.input.case_4_1.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_4_1.result.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_4_1.result.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase4OnlyRight() {
        String rightResponse = given()
            .body(testData.read("$.input.case_4_2.right.json").toString())
            .post(testData.read("$.input.case_4_2.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_4_2.right.response.status", "$.input.case_4_2.right.response.content",
            rightResponse);
        String response = given()
            .get(testData.read("$.input.case_4_2.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(response);
        assertEquals(testData.read("$.input.case_4_2.result.response.status").toString(), jsonResponse.read("$.status"));
        assertEquals(testData.read("$.input.case_4_2.result.response.content").toString(),
            jsonResponse.read("$.content"));
    }

    @Test
    public void testCase5DifferentSize() {
        String leftResponse = given()
            .body(testData.read("$.input.case_5.left.json").toString())
            .post(testData.read("$.input.case_5.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_7.left.response.status", "$.input.case_7.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_5.right.json").toString())
            .post(testData.read("$.input.case_5.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_5.right.response.status", "$.input.case_5.right.response.content",
            rightResponse);
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
        assertEquals(testData.read("$.input.case_5.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_5.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
    }

    @Test
    public void testCase6NotFound() {
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
        assertEquals(testData.read("$.input.case_6.result.response.content").toString(),
            jsonResponse.read("$.content"));

    }

    @Test
    public void testCase7CompletedComparisonUpdate() {
        String leftResponse = given()
            .body(testData.read("$.input.case_7.left.json").toString())
            .post(testData.read("$.input.case_7.left.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_7.left.response.status", "$.input.case_7.left.response.content",
            leftResponse);
        String rightResponse = given()
            .body(testData.read("$.input.case_7.right.json").toString())
            .post(testData.read("$.input.case_7.right.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        assertBasicResponse("$.input.case_7.right.response.status", "$.input.case_7.right.response.content",
            rightResponse);
        String comparisonResult = given()
            .get(testData.read("$.input.case_7.result.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read("$.input.case_7.result.response.status").toString(),
            jsonResponse.read("$.status").toString());
        assertEquals(testData.read("$.input.case_7.result.response.content.ID").toString(),
            jsonResponse.read("$.content.ID"));
        assertEquals(testData.read("$.input.case_7.result.response.content.result").toString(),
            jsonResponse.read("$.content.result"));
        assertEquals(testData.read("$.input.case_7.result.response.content.differences").toString(),
            jsonResponse.read("$.content.differences"));
        String rightReuploadResponse = given()
            .body(testData.read("$.input.case_7.right_reupload.json").toString())
            .post(testData.read("$.input.case_7.right_reupload.url").toString())
            .then()
            .statusCode(200)
            .extract()
            .response()
            .asString();
        DocumentContext rightReuploadJson = JsonPath.parse(rightReuploadResponse);
        assertEquals(testData.read("$.input.case_7.right_reupload.response.status").toString(),
            rightReuploadJson.read("$.status").toString());
        assertEquals(testData.read("$.input.case_7.right_reupload.response.content").toString(),
            rightReuploadJson.read("$.content"));
    }

    private void assertBasicResponse(String keyStatus, String keyContent, String response) {
        DocumentContext ctx = JsonPath.parse(response);
        assertEquals(testData.read(keyStatus).toString(), ctx.read("$.status"));
        assertEquals(testData.read(keyContent).toString(), ctx.read("$.content"));
    }
}
