/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static org.mockito.Mockito.mock;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.ws.rs.BadRequestException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link WebLoggerResource}.
 */
public final class WebLoggerResourceTests {

    @Test
    public void testGoodLogContent() throws IOException, ParseException {
        testLogContent("name", FieldTypes.STRING);

        // no exception thrown is a pass
    }

    @Test(expected = BadRequestException.class)
    public void testBadLogContent() throws IOException, ParseException {
        testLogContent("name", FieldTypes.INT);
    }

    private void testLogContent(String field, FieldTypes type) throws ParseException {
        Appender mockAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger("analytics");
        root.addAppender(mockAppender);

        List<LoggerField> list = Lists.newArrayList();
        list.add(ImmutableLoggerField.builder().field(field).type(type).build());

        String eventJson = "{\"name\": \"storm\"}";

        WebLoggerConfiguration webLoggerConfiguration =
                ImmutableWebLoggerConfiguration.builder().enabled(true).fields(list).build();

        WebLoggerResource webLoggerResource = new WebLoggerResource(webLoggerConfiguration);

        webLoggerResource.logContent(eventJson);
    }
}
