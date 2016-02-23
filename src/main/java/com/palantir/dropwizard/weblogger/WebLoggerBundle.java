/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Bundle to expose an API endpoint to log analytics events.
 */
public final class WebLoggerBundle implements ConfiguredBundle<WebLoggerConfigurationProvider> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // intentionally left blank
    }

    @Override
    public void run(WebLoggerConfigurationProvider config, Environment environment) {
        checkNotNull(config);
        checkNotNull(environment);

        if (config.getWebLogger().getEnabled().or(true)) {
            environment.jersey().register(new WebLoggerResource(config.getWebLogger()));
        }
    }
}
