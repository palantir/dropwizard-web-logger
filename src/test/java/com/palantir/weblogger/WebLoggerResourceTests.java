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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.Appender;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.ws.rs.BadRequestException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link WebLoggerResource}.
 */
public final class WebLoggerResourceTests {
    private static final Set<String> EVENTS = ImmutableSet.of("specifiedEventName", "someOtherEvent");
    private static final WebLoggerConfiguration WEB_LOGGER_CONFIG =
            ImmutableWebLoggerConfiguration.builder().eventNames(EVENTS).build();
    private static final WebLoggerResource WEB_LOGGER = new WebLoggerResource(WEB_LOGGER_CONFIG);

    @Before
    public void setUp() {
        Appender mockAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger("analytics");
        root.addAppender(mockAppender);
    }

    @Test
    public void testLogContentFailOnUnknownEvent() {
        JSONObject eventJson = new JSONObject(ImmutableMap.of("noEventName", "unspecified", "another", "something"));

        try {
            WEB_LOGGER.logContent("jump", eventJson);
            fail();
        } catch (BadRequestException e) {
            // expected
        }
    }

    @Test
    public void testLogContent() {
        JSONObject eventJson = new JSONObject(ImmutableMap.of("eventName", "unspecified", "another", "something"));
        WEB_LOGGER.logContent("specifiedEventName", eventJson);
    }
}
