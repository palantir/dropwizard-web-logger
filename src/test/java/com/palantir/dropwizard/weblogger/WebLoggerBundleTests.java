/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * Tests for {@link WebLoggerBundle}.
 */
public final class WebLoggerBundleTests {

    public static final WebLoggerConfigurationProvider PROVIDER_CONFIG =
            new WebLoggerConfigurationProvider() {
        @Override
        public WebLoggerConfiguration getWebLogger() {
            Set<LoggerEvent> list = Collections.<LoggerEvent>emptySet();
            return ImmutableWebLoggerConfiguration.builder().events(list).build();
        }
    };

    @Test
    public void testAddsWebLoggerResource() {
        Environment environment = mock(Environment.class);
        JerseyEnvironment jerseyEnvironment = mock(JerseyEnvironment.class);
        when(environment.jersey()).thenReturn(jerseyEnvironment);
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        when(environment.getObjectMapper()).thenReturn(objectMapper);
        Bootstrap<?> bootstrap = mock(Bootstrap.class);

        WebLoggerBundle webLoggerBundle = new WebLoggerBundle();
        webLoggerBundle.initialize(bootstrap);
        webLoggerBundle.run(PROVIDER_CONFIG, environment);
        verify(jerseyEnvironment).register(isA(WebLoggerResource.class));
    }

    @Test
    public void testDoesNotAddWebLoggerResourceWhenDisabled() {
        JerseyEnvironment jerseyEnvironment = mock(JerseyEnvironment.class);
        Environment environment = mock(Environment.class);
        when(environment.jersey()).thenReturn(jerseyEnvironment);
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        when(environment.getObjectMapper()).thenReturn(objectMapper);
        Bootstrap<?> bootstrap = mock(Bootstrap.class);

        WebLoggerConfigurationProvider config = new WebLoggerConfigurationProvider() {
            @Override
            public WebLoggerConfiguration getWebLogger() {
                List<LoggerEvent> list = Lists.newArrayList();
                return ImmutableWebLoggerConfiguration.builder().enabled(false).events(list).build();
            }
        };

        WebLoggerBundle webLoggerBundle = new WebLoggerBundle();
        webLoggerBundle.initialize(bootstrap);
        webLoggerBundle.run(config, environment);
        verify(jerseyEnvironment, never()).register(isA(WebLoggerResource.class));
    }
}
