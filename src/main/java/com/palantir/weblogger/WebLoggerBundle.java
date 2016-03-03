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

        if (config.getWebLogger().getEnabled()) {
            environment.jersey().register(new WebLoggerResource(config.getWebLogger()));
        }
    }
}
