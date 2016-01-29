/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static com.google.common.base.Preconditions.checkNotNull;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.DropwizardLayout;
import io.dropwizard.logging.FileAppenderFactory;
import java.util.TimeZone;

/**
 * Defines appender for logging analytics events.
 *
 * Dropwizard does not support multiple loggers with different destination
 * appenders, so non-analytics events need to be filtered out.
 *
 */
@JsonTypeName(AnalyticsAppenderFactory.ANALYTICS_APPENDER)
public final class AnalyticsAppenderFactory extends FileAppenderFactory {

    public static final String ANALYTICS_LOGGER = "analytics";
    public static final String ANALYTICS_APPENDER = "web-logger";

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context, String applicationName, Layout<ILoggingEvent> layout) {
        checkNotNull(context);
        checkNotNull(applicationName);

        // the following lines were modelled after FileAppenderFactory's build() method
        FileAppender<ILoggingEvent> appender = buildAppender(context);
        appender.setName(ANALYTICS_APPENDER);
        appender.setAppend(true);
        appender.setContext(context);
        appender.setPrudent(false);

        LayoutWrappingEncoder<ILoggingEvent> layoutEncoder = new LayoutWrappingEncoder<>();
        layoutEncoder.setLayout(layout == null ? buildLayout(context, getTimeZone()) : layout);
        appender.setEncoder(layoutEncoder);

        // instead of adding a threshold filter, filter out all events not directed at the analytics logger
        appender.addFilter(new AnalyticsFilter());
        appender.stop();
        appender.start();

        // prevent events from the analytics logger from propagating further up
        Logger analyticsLogger = context.getLogger(ANALYTICS_LOGGER);
        analyticsLogger.setAdditive(false);
        analyticsLogger.setLevel(Level.ALL);
        Appender<ILoggingEvent> asyncAppender = wrapAsync(appender);
        analyticsLogger.addAppender(asyncAppender);

        return asyncAppender;
    }

    @Override
    protected DropwizardLayout buildLayout(LoggerContext context, TimeZone timeZone) {
        DropwizardLayout formatter = new DropwizardLayout(context, timeZone);
        formatter.setPattern("%m%n");
        formatter.start();
        return formatter;
    }

    public static final class AnalyticsFilter extends Filter<ILoggingEvent> {
        @Override
        public FilterReply decide(ILoggingEvent event) {
            return event.getLoggerName().equals(ANALYTICS_LOGGER) ? FilterReply.ACCEPT : FilterReply.DENY;
        }
    }
}
