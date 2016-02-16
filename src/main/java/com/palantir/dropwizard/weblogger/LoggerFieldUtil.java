/*
 * Copyright 2015 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.dropwizard.weblogger;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for {@link WebLoggerResource}.
 */
public final class LoggerFieldUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggerFieldUtil.class);

    private LoggerFieldUtil() { }

    public static boolean validateJsonObject(List<LoggerEvent> configFields, String eventJson) {
        checkNotNull(configFields);
        checkNotNull(eventJson);

        JSONObject jsonEventObject = new JSONObject(eventJson);

//        for (LoggerField field : configFields) {
//            Object fieldValue = jsonEventObject.get(field.getField());
//
//            switch (field.getType()) {
//                case INT:
//                    if (!(fieldValue instanceof Integer)) {
//                        logger.error("Object should be of type 'int' for field '"
//                                + field.getField() + "'. Received value '" + fieldValue
//                                + "' which is not of type 'int'.");
//                        return false;
//                    }
//                    break;
//                case STRING:
//                    if (!(fieldValue instanceof String)) {
//                        logger.error("Object should be of type 'string' for field '"
//                                + field.getField() + "'. Received value '" + fieldValue
//                                + "' which is not of type 'string'.");
//                        return false;
//                    }
//                    break;
//                case DATE:
//                    try {
//                        Long.parseLong(fieldValue.toString());
//                    } catch (NumberFormatException e) {
//
//                        logger.error("Object should be of type 'date' in epoch time for field '"
//                                + field.getField() + "'. Received value '" + fieldValue
//                                + "' which is not of type 'date' in epoch time.");
//                        return false;
//                    }
//
//                    break;
//                default:
//                    System.out.println(field.getType() + " is not a valid field type. "
//                            + "Must be 'int', 'string', or 'date'");
//                    return false;
//            }
//        }
        return true;
    }

    public static String extractLogLine(JSONObject obj, List<LoggerField> fields) {
        checkNotNull(obj);
        checkNotNull(fields);

        String logLine = "";
        for (LoggerField field : fields) {
            if (!logLine.equals("")) {
                logLine += ", ";
            }

            switch (field.getType()) {
                case INT:
                    logLine += field.getField() + ": " + obj.get(field.getField());
                    break;
                case STRING:
                    logLine += field.getField() + ": '" + obj.get(field.getField()) + "'";
                    break;
                case DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String date = sdf.format(new Date(Long.parseLong(obj.get(field.getField()).toString())));
                    logLine += field.getField() + ": '" + date + "'";
                    break;
                default:
                    logger.error("Unsupported log type " + field.getType() + ". Must be "
                            + "of type: ['int', 'string', 'date']");
                    break;
            }

        }
        return logLine;
    }
}
