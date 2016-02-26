/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static org.mockito.Mockito.mock;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.BadRequestException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link WebLoggerResource}.
 */
public final class WebLoggerResourceTests {

    @Test(expected = BadRequestException.class)
    public void testBadFieldsInLogContent() throws IOException, ParseException {
        Appender mockAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger("analytics");
        root.addAppender(mockAppender);

        Set<LoggerEvent> events = new HashSet<LoggerEvent>();

        LoggerEvent loggerEvent = new LoggerEvent() {
            @Override
            public Set<String> getFields() {
                return new HashSet<String>(Arrays.asList("user", "title"));
            }

            @Override
            public String getType() {
                return "userLogin";
            }

            @Override
            public boolean getEnabled() {
                return true;
            }
        };

        events.add(loggerEvent);

        WebLoggerConfiguration webLoggerConfiguration =
                ImmutableWebLoggerConfiguration.builder().events(events).build();

        WebLoggerResource webLoggerResource = new WebLoggerResource(webLoggerConfiguration);

        String eventJson = "{\"badUser\": \"storm\"}";
        webLoggerResource.logContent(eventJson);

    }

    @Test
    public void testGoodFieldsInLogContent() throws IOException, ParseException {
        Appender mockAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger("analytics");
        root.addAppender(mockAppender);

        Set<LoggerEvent> events = new HashSet<LoggerEvent>();

        LoggerEvent loggerEvent = new LoggerEvent() {
            @Override
            public Set<String> getFields() {
                return new HashSet<String>(Arrays.asList("user", "title"));
            }

            @Override
            public String getType() {
                return "userLogin";
            }

            @Override
            public boolean getEnabled() {
                return true;
            }
        };

        events.add(loggerEvent);

        WebLoggerConfiguration webLoggerConfiguration =
                ImmutableWebLoggerConfiguration.builder().events(events).build();

        WebLoggerResource webLoggerResource = new WebLoggerResource(webLoggerConfiguration);

        String eventJson = "{\"user\": \"storm\",\"title\": \"scientist\"}";
        webLoggerResource.logContent(eventJson);

        // Doesn't throw exception..

    }

}
