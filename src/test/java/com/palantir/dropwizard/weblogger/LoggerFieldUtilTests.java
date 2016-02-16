/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import java.util.List;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Tests for {@link LoggerFieldUtil}.
 */
public final class LoggerFieldUtilTests {
    @Test
    public void validateGoodIntegerInput_validateJsonObjectTest() {
        validateTrueForGoodInput(ImmutableLoggerField.builder().field("id").type(FieldTypes.INT).build(),
                "{ \"id\": 123532 }");
    }

    @Test
    public void validateBadIntegerInput_validateJsonObjectTest() {
        validateNotTrueForBadInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.INT).build(), "{ \"id\": \"awefaewf\" }");
    }

    @Test
    public void validateGoodStringInput_validateJsonObjectTest() {
        validateTrueForGoodInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build(), "{ \"id\": \"awefaewf\" }");
    }

    @Test
    public void validateBadStringInput_validateJsonObjectTest() {
        validateNotTrueForBadInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build(), "{ \"id\": 34432 }");
    }

    @Test
    public void validateGoodDateInput_validateJsonObjectTest() {
        validateTrueForGoodInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.DATE).build(), "{ \"id\": 1448401914000 }");
        validateTrueForGoodInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.DATE).build(),
                "{ \"id\": \"1448401914000\" }");
    }

    @Test
    public void validateBadDateInput_validateJsonObjectTest() {
        validateNotTrueForBadInput(
                ImmutableLoggerField.builder().field("id").type(FieldTypes.DATE).build(), "{ \"id\": \"awefaewf\" }");
    }

//    @Test
//    public void multiGoodInput_validateJsonObjectTest() {
//        List<LoggerField> fields = Lists.newArrayList();
//        fields.add(ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build());
//        fields.add(ImmutableLoggerField.builder().field("name").type(FieldTypes.STRING).build());
//        fields.add(ImmutableLoggerField.builder().field("description").type(FieldTypes.STRING).build());
//        fields.add(ImmutableLoggerField.builder().field("mynumber").type(FieldTypes.INT).build());
//        fields.add(ImmutableLoggerField.builder().field("mydate").type(FieldTypes.DATE).build());
//
//        WebLoggerConfiguration config =
//                ImmutableWebLoggerConfiguration.builder().enabled(true).fields(fields).build();
//
//        String eventJson = "{\"id\":\"12aoi312\", "
//                + "\"name\": \"bruce wayne\", "
//                + "\"description\": \"this is a description\", "
//                + "\"mynumber\": 123, "
//                + "\"mydate\": 123552 }";
//
//        boolean validateJsonObject = LoggerFieldUtil.validateJsonObject(config.getFields(), eventJson);
//
//        assertTrue(validateJsonObject);
//    }

    @Test
    public void multiBadInput_validateJsonObjectTest() {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build());
        fields.add(ImmutableLoggerField.builder().field("description").type(FieldTypes.STRING).build());
        fields.add(ImmutableLoggerField.builder().field("mynumber").type(FieldTypes.INT).build());
        fields.add(ImmutableLoggerField.builder().field("mydate").type(FieldTypes.DATE).build());

        WebLoggerConfiguration config =
                ImmutableWebLoggerConfiguration.builder().enabled(true).fields(fields).build();

        String eventJson = "{\"id\":\"12aoi312\", "
                + "\"name\": \"bruce wayne\", "
                + "\"description\": \"this is a description\", "
                + "\"mynumber\": 123, "
                + "\"mydate\": 123552 }";

        boolean validateJsonObject = LoggerFieldUtil.validateJsonObject(config.getFields(), eventJson);

        assertTrue(validateJsonObject);
    }

    @Test
    public void extractIntegerLines_extractLogLineTest() {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(ImmutableLoggerField.builder().field("mynumber").type(FieldTypes.INT).build());

        JSONObject obj = new JSONObject("{ \"mynumber\": 123 }");
        String logLine = LoggerFieldUtil.extractLogLine(obj, fields);

        assertEquals("mynumber: 123", logLine);
    }

    @Test
    public void extractStringLines_extractLogLineTest() {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build());

        JSONObject obj = new JSONObject("{ \"id\": \"10cawe24\" }");
        String logLine = LoggerFieldUtil.extractLogLine(obj, fields);

        assertEquals("id: '10cawe24'", logLine);
    }

    @Test
    public void extractDateLines_extractLogLineTest() {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(ImmutableLoggerField.builder().field("mydate").type(FieldTypes.DATE).build());

        JSONObject obj = new JSONObject("{ \"mydate\": \"1448401914000\" }");
        String logLine = LoggerFieldUtil.extractLogLine(obj, fields);

        assertEquals("mydate: \'2015-11-24 21:51:54 UTC\'", logLine);
    }

    @Test
    public void extractMultiLines_extractLogLineTest() {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(ImmutableLoggerField.builder().field("id").type(FieldTypes.STRING).build());
        fields.add(ImmutableLoggerField.builder().field("name").type(FieldTypes.STRING).build());
        fields.add(ImmutableLoggerField.builder().field("description").type(FieldTypes.STRING).build());
        fields.add(ImmutableLoggerField.builder().field("mynumber").type(FieldTypes.INT).build());
        fields.add(ImmutableLoggerField.builder().field("mydate").type(FieldTypes.DATE).build());

        JSONObject obj = new JSONObject("{\"id\":\"12aoi312\", "
                + "\"name\": \"bruce wayne\", "
                + "\"description\": \"this is a description\", "
                + "\"mynumber\": 123, "
                + "\"mydate\": 1448401914000 }");

        String logLine = LoggerFieldUtil.extractLogLine(obj, fields);

        assertEquals("id: \'12aoi312\', name: \'bruce wayne\', description: "
                + "\'this is a description\', mynumber: 123, "
                + "mydate: '2015-11-24 21:51:54 UTC'", logLine);
    }

    private void validateNotTrueForBadInput(LoggerField loggerField, String eventJson) {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(loggerField);
        WebLoggerConfiguration config =
                ImmutableWebLoggerConfiguration.builder().enabled(true).fields(fields).build();
        boolean validateJsonObject = LoggerFieldUtil.validateJsonObject(config.getFields(), eventJson);
        assertFalse(validateJsonObject);
    }

    private void validateTrueForGoodInput(LoggerField loggerField, String eventJson) {
        List<LoggerField> fields = Lists.newArrayList();
        fields.add(loggerField);
        WebLoggerConfiguration config =
                ImmutableWebLoggerConfiguration.builder().enabled(true).fields(fields).build();
        boolean validateJsonObject = LoggerFieldUtil.validateJsonObject(config.getFields(), eventJson);
        assertTrue(validateJsonObject);
    }

}
