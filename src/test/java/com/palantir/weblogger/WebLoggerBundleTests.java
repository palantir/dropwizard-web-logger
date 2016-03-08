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

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.Collections;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link WebLoggerBundle}.
 */
public final class WebLoggerBundleTests {

    private static final Bootstrap<?> BOOTSTRAP = mock(Bootstrap.class);
    private static final Environment ENVIRONMENT = mock(Environment.class);
    private static final WebLoggerConfigurationProvider PROVIDER_CONFIG =
            new WebLoggerConfigurationProvider() {
        @Override
        public WebLoggerConfiguration getWebLogger() {
            Set<String> list = Collections.<String>emptySet();
            return ImmutableWebLoggerConfiguration.builder().eventNames(list).build();
        }
    };
    private JerseyEnvironment jerseyEnvironment;

    @Before
    public void setUp() {
        jerseyEnvironment = mock(JerseyEnvironment.class);
        when(ENVIRONMENT.jersey()).thenReturn(jerseyEnvironment);
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        when(ENVIRONMENT.getObjectMapper()).thenReturn(objectMapper);
    }

    @Test
    public void testAddsWebLoggerResource() {
        WebLoggerBundle webLoggerBundle = new WebLoggerBundle();
        webLoggerBundle.initialize(BOOTSTRAP);
        webLoggerBundle.run(PROVIDER_CONFIG, ENVIRONMENT);
        verify(jerseyEnvironment).register(isA(WebLoggerResource.class));
    }

    @Test
    public void testDoesNotAddWebLoggerResourceWhenDisabled() {
        WebLoggerConfigurationProvider config = new WebLoggerConfigurationProvider() {
            @Override
            public WebLoggerConfiguration getWebLogger() {
                Set<String> list = Collections.<String>emptySet();
                return ImmutableWebLoggerConfiguration.builder().enabled(false).eventNames(list).build();
            }
        };

        WebLoggerBundle webLoggerBundle = new WebLoggerBundle();
        webLoggerBundle.initialize(BOOTSTRAP);
        webLoggerBundle.run(config, ENVIRONMENT);
        verify(jerseyEnvironment, never()).register(isA(WebLoggerResource.class));
    }
}
