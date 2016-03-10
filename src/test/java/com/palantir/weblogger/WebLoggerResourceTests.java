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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import java.io.IOException;
import java.text.ParseException;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link WebLoggerResource}.
 */
public final class WebLoggerResourceTests {

    @SuppressWarnings("unchecked")
    @Test
    public void testGoodFieldsInLogContent() throws IOException, ParseException {
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        Logger root = (Logger) LoggerFactory.getLogger("analytics");
        root.addAppender(mockAppender);

        WebLoggerResource webLoggerResource = new WebLoggerResource();

        String eventJson = "{\"eventName\": \"specifiedEventName\", \"another\": \"something\"}";
        webLoggerResource.logContent("specifiedEventName", eventJson);

        // Doesn't throw exception..
    }
}
