/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * POJO for deserializing logger fields.
 */

@Value.Immutable
@JsonSerialize(as = ImmutableLoggerField.class)
@JsonDeserialize(as = ImmutableLoggerField.class)
public abstract class LoggerField {
    public abstract String getField();

    public abstract FieldTypes getType();
}
