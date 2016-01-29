/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.jackson.Jackson;
import java.text.ParseException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
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
    public void logContent(String eventJson) throws ParseException {
        if (LoggerFieldUtil.validateJsonObject(this.config.getFields(), eventJson)) {
            analyticsLogger.info(String.valueOf(eventJson));
        } else {
            throw new BadRequestException();
        }
    }
}
