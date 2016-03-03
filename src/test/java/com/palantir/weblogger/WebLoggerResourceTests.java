/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.weblogger;

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
