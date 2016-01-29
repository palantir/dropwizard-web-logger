/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

/**
 * Implemented by the consuming application's configuration class to ensure this bundle specific
 * configuration is provided.
 */
public interface WebLoggerConfigurationProvider {
    WebLoggerConfiguration getWebLogger();
}
