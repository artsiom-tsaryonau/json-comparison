package com.assignment.diff.jsoncomparison;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Tests for {@link JsonComparisonApplication}.
 * <p/>
 * Copyright (C) 2019
 * <p/>
 * Date: 07/20/2019
 *
 * @author Artsiom Tsaryonau
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonComparisonApplicationTest {

    @Autowired(required = false)
    private JsonComparisonController comparisonController;

    @Test
    public void contextLoads() {
        assertNotNull(comparisonController);
    }
}
