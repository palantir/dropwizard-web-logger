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

package com.palantir.weblogger.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.weblogger.WebLoggerBundle;
import com.palantir.weblogger.WebLoggerConfigurable;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public final class Example {

    private Example() {
        // utility class for testing
    }

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(args);
    }

    private static class ExampleApplication extends Application<ExampleConfiguration> {
        public static void main(String[] args) throws Exception {
            new ExampleApplication().run(args);
        }

        @Override
        public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
            bootstrap.addBundle(new WebLoggerBundle());
        }

        @Override
        public void run(ExampleConfiguration configuration, Environment environment) {}
    }

    public static final class ExampleConfiguration extends Configuration implements WebLoggerConfigurable {

        private final boolean webLoggerEnabled;

        @JsonCreator
        public ExampleConfiguration(@JsonProperty("webLoggerEnabled") boolean webLoggerEnabled) {
            this.webLoggerEnabled = webLoggerEnabled;
        }

        @Override
        public boolean isWebLoggerEnabled() {
            return this.webLoggerEnabled;
        }
    }
}
