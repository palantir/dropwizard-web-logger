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

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.jackson.Jackson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource for logging analytics events.
 */
@Path("web-logger")
@Consumes(MediaType.APPLICATION_JSON)
public final class WebLoggerResource {

    private static final Logger analyticsLogger = LoggerFactory.getLogger(AnalyticsAppenderFactory.ANALYTICS_LOGGER);

    private final WebLoggerConfiguration config;

    public WebLoggerResource(WebLoggerConfiguration config) {
        checkNotNull(config);

        // serialise dates as ISO 8601
        Jackson.newObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        this.config = config;
    }

    @POST
    public void logContent(String eventJsonString) throws ParseException {

        JSONObject jsonEvent = new JSONObject(eventJsonString);
        Set<String> logLineFields = jsonEvent.keySet();

        boolean validEvent = false;
        String eventType = "";
        boolean eventLogEnabled = false;

        for (LoggerEvent event : config.getEvents()) {
            if (event.getFields().containsAll(logLineFields)) {
                validEvent = true;
                eventType = event.getType();
                eventLogEnabled = event.getEnabled();
            }
        }

        if (!validEvent) {
            generateFieldsDontMatchError(jsonEvent);
        }

        if (eventLogEnabled) {
            jsonEvent = addFixedFields(jsonEvent);
            jsonEvent = addEventType(jsonEvent, eventType);

            analyticsLogger.info(jsonEvent.toString());
        }
    }

    private void generateFieldsDontMatchError(JSONObject jsonEvent) {
        StringBuffer buffer = new StringBuffer();
        for (LoggerEvent event : config.getEvents()) {
            buffer.append(event.getFields().toString() + " ");
        }
        String fieldSets = buffer.toString();

        throw new BadRequestException("It's likely that the fields in the log provided don't "
                + "match the server's configuration. Please adjust your log fields or the"
                + " configuration in your <server>.yml file. Possible choices are: "
                + fieldSets + ". You provided: " + jsonEvent.keySet());
    }

    private JSONObject addEventType(JSONObject jsonEvent, String eventType) {
        jsonEvent.put("EventType", eventType);
        return jsonEvent;
    }

    private JSONObject addFixedFields(JSONObject jsonEvent) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        ft.setTimeZone(TimeZone.getTimeZone("UTC"));
        jsonEvent.put("timestamp", ft.format(new Date()));
        return jsonEvent;
    }
}