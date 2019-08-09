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
 * The tests look similar to a regular BDD scenarios.
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
        var data = new String(Files.readAllBytes(
            Paths.get(ClassLoader.getSystemResource("test_data.json").toURI())));
        testData = JsonPath.parse(data);
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testCase1DifferentJson() {
        String leftResponse = makeUploadRequest("$.input.case_1.left.url", "$.input.case_1.left.json",
            "$.input.case_1.left.response.code");
        assertCommonResponse("$.input.case_1.left.response.status", "$.input.case_1.left.response.content",
            leftResponse);

        String rightResponse = makeUploadRequest("$.input.case_1.right.url", "$.input.case_1.right.json",
            "$.input.case_1.right.response.code");
        assertCommonResponse("$.input.case_1.right.response.status", "$.input.case_1.right.response.content",
            rightResponse);

        String comparisonResult = makeComparisonResultRequest("$.input.case_1.result.url",
            "$.input.case_1.result.response.code");
        assertResultWithDifferences("$.input.case_1.result.response.status",
            "$.input.case_1.result.response.content.ID", "$.input.case_1.result.response.content.result",
            "$.input.case_1.result.response.content.differences", comparisonResult);
    }

    @Test
    public void testCase2SameJson() {
        String leftResponse = makeUploadRequest("$.input.case_2.left.url", "$.input.case_2.left.json",
            "$.input.case_2.left.response.code");
        assertCommonResponse("$.input.case_2.left.response.status", "$.input.case_2.left.response.content",
            leftResponse);

        String rightResponse = makeUploadRequest("$.input.case_2.right.url", "$.input.case_2.right.json",
            "$.input.case_2.right.response.code");
        assertCommonResponse("$.input.case_2.right.response.status", "$.input.case_2.right.response.content",
            rightResponse);

        String comparisonResult = makeComparisonResultRequest("$.input.case_2.result.url",
            "$.input.case_2.result.response.code");
        assertBasicResult("$.input.case_2.result.response.status", "$.input.case_2.result.response.content.ID",
            "$.input.case_2.result.response.content.result", comparisonResult);
    }

    @Test
    public void testCase3OnlyLeft() {
        String leftResponse = makeUploadRequest("$.input.case_3_1.left.url", "$.input.case_3_1.left.json",
            "$.input.case_3_1.left.response.code");
        assertCommonResponse("$.input.case_3_1.left.response.status", "$.input.case_3_1.left.response.content",
            leftResponse);

        String response = makeComparisonResultRequest("$.input.case_3_1.result.url",
            "$.input.case_3_1.result.response.code");
        assertCommonResponse("$.input.case_3_1.result.response.status", "$.input.case_3_1.result.response.content",
            response);
    }

    @Test
    public void testCase3OnlyRight() {
        String rightResponse = makeUploadRequest("$.input.case_3_2.right.url", "$.input.case_3_2.right.json",
            "$.input.case_3_2.right.response.code");
        assertCommonResponse("$.input.case_3_2.right.response.status", "$.input.case_3_2.right.response.content",
            rightResponse);

        String response = makeComparisonResultRequest("$.input.case_3_2.result.url",
            "$.input.case_3_2.result.response.code");
        assertCommonResponse("$.input.case_3_2.result.response.status", "$.input.case_3_2.result.response.content",
            response);
    }

    @Test
    public void testCase4DifferentSize() {
        String leftResponse = makeUploadRequest("$.input.case_4.left.url", "$.input.case_4.left.json",
            "$.input.case_4.left.response.code");
        assertCommonResponse("$.input.case_4.left.response.status", "$.input.case_4.left.response.content",
            leftResponse);

        String rightResponse = makeUploadRequest("$.input.case_4.right.url", "$.input.case_4.right.json",
            "$.input.case_4.right.response.code");
        assertCommonResponse("$.input.case_4.right.response.status", "$.input.case_4.right.response.content",
            rightResponse);

        String comparisonResult = makeComparisonResultRequest("$.input.case_4.result.url",
            "$.input.case_4.result.response.code");
        assertBasicResult("$.input.case_4.result.response.status", "$.input.case_4.result.response.content.ID",
            "$.input.case_4.result.response.content.result", comparisonResult);
    }

    @Test
    public void testCase5NotFound() {
        String comparisonResult = makeComparisonResultRequest("$.input.case_5.result.url",
            "$.input.case_5.result.response.code");
        assertCommonResponse("$.input.case_5.result.response.status", "$.input.case_5.result.response.content",
            comparisonResult);
    }

    @Test
    public void testCase6CompletedComparisonUpdate() {
        String leftResponse = makeUploadRequest("$.input.case_6.left.url", "$.input.case_6.left.json",
            "$.input.case_6.left.response.code");
        assertCommonResponse("$.input.case_6.left.response.status", "$.input.case_6.left.response.content",
            leftResponse);

        String rightResponse = makeUploadRequest("$.input.case_6.right.url", "$.input.case_6.right.json",
            "$.input.case_6.right.response.code");
        assertCommonResponse("$.input.case_6.right.response.status", "$.input.case_6.right.response.content",
            rightResponse);

        String comparisonResult = makeComparisonResultRequest("$.input.case_6.result.url",
            "$.input.case_6.result.response.code");
        assertResultWithDifferences("$.input.case_6.result.response.status",
            "$.input.case_6.result.response.content.ID", "$.input.case_6.result.response.content.result",
            "$.input.case_6.result.response.content.differences", comparisonResult);

        String rightReuploadResponse = makeUploadRequest("$.input.case_6.right_reupload.url",
            "$.input.case_6.right_reupload.json", "$.input.case_6.right_reupload.response.code");
        assertCommonResponse("$.input.case_6.right_reupload.response.status",
            "$.input.case_6.right_reupload.response.content", rightReuploadResponse);
    }

    @Test
    public void testCase7InternalError() {
        String leftResponse = makeUploadRequest("$.input.case_7.left.url", null, "$.input.case_7.left.response.code");
        assertCommonResponse("$.input.case_7.left.response.status", "$.input.case_7.left.response.content",
            leftResponse);

    }

    private void assertCommonResponse(String keyStatus, String keyContent, String response) {
        DocumentContext ctx = JsonPath.parse(response);
        assertEquals(testData.read(keyStatus).toString(), ctx.read("$.status"));
        assertEquals(testData.read(keyContent).toString(), ctx.read("$.content"));
    }

    private String makeUploadRequest(String urlKey, String bodyKey, String expectedStatusKey) {
        if (null == bodyKey) {
            return given()
                .post(testData.read(urlKey).toString())
                .then()
                .statusCode(testData.read(expectedStatusKey, int.class))
                .extract()
                .response()
                .asString();
        } else {
            return given()
                .body(testData.read(bodyKey).toString())
                .post(testData.read(urlKey).toString())
                .then()
                .statusCode(testData.read(expectedStatusKey, int.class))
                .extract()
                .response()
                .asString();
        }
    }

    private String makeComparisonResultRequest(String urlKey, String expectedCodeKey) {
        return given()
            .get(testData.read(urlKey).toString())
            .then()
            .statusCode(testData.read(expectedCodeKey, int.class))
            .extract()
            .response()
            .asString();
    }

    private void assertBasicResult(String responseStatusKey, String responseIdKey, String resultKey,
                                   String comparisonResult) {
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read(responseStatusKey).toString(), jsonResponse.read("$.status").toString());
        assertEquals(testData.read(responseIdKey).toString(), jsonResponse.read("$.content.ID"));
        assertEquals(testData.read(resultKey).toString(), jsonResponse.read("$.content.result"));
    }

    private void assertResultWithDifferences(String statusKey, String idKey, String resultKey,
                                             String differencesKey, String comparisonResult) {
        DocumentContext jsonResponse = JsonPath.parse(comparisonResult);
        assertEquals(testData.read(statusKey).toString(), jsonResponse.read("$.status").toString());
        assertEquals(testData.read(idKey).toString(), jsonResponse.read("$.content.ID"));
        assertEquals(testData.read(resultKey).toString(), jsonResponse.read("$.content.result"));
        assertEquals(testData.read(differencesKey).toString(), jsonResponse.read("$.content.differences"));
    }
}
