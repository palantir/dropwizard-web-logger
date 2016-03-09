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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import org.immutables.value.Value;

/**
 * Configuration for {@link WebLoggerBundle}.
 */

@Value.Immutable
@JsonDeserialize(as = ImmutableWebLoggerConfiguration.class)
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        builderVisibility = Value.Style.BuilderVisibility.PACKAGE)
@SuppressWarnings("checkstyle:designforextension")
public abstract class WebLoggerConfiguration {

    @Value.Default
    public boolean enabled() {
        return true;
    }

    public abstract Set<String> eventNames();

    // hides implementation details
    public static Builder builder() {
        return ImmutableWebLoggerConfiguration.builder();
    }

    // hides implementation details
    public interface Builder {

        Builder enabled(boolean enabled);

        Builder from(WebLoggerConfiguration otherConfig);

        WebLoggerConfiguration build();
    }
}
