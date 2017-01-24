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

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.jackson.Jackson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource for logging analytics events.
 */
@Path("web-logger/events")
public final class WebLoggerResource {

    private static final Logger analyticsLogger = LoggerFactory.getLogger(AnalyticsAppenderFactory.ANALYTICS_LOGGER);

    public WebLoggerResource() {

        // serialise dates as ISO 8601
        Jackson.newObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @POST
    @Path("{eventName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void logContent(@PathParam("eventName") String eventName, String jsonStringEvent) throws ParseException {
        JSONObject jsonEvent = new JSONObject(jsonStringEvent);

        JSONObject jsonLog = addEventName(jsonEvent, eventName);
        jsonLog = addTimestamp(jsonLog);

        analyticsLogger.info(jsonLog.toString());
    }

    @POST
    @Path("batch")
    @Consumes(MediaType.APPLICATION_JSON)
    public void logBatch(Map<String, String> events) throws ParseException {
        for (Map.Entry<String, String> event : events.entrySet()) {
            logContent(event.getKey(), event.getValue());
        }
    }

    private JSONObject addEventName(JSONObject jsonEvent, String eventName) {
        jsonEvent.put("eventName", eventName);
        return jsonEvent;
    }

    private JSONObject addTimestamp(JSONObject jsonEvent) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        ft.setTimeZone(TimeZone.getTimeZone("UTC"));
        jsonEvent.put("timestamp", ft.format(new Date()));
        return jsonEvent;
    }
}
