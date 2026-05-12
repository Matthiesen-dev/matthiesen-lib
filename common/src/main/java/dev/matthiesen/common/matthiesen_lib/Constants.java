package dev.matthiesen.common.matthiesen_lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains constants and logging utilities for the Matthiesen Lib mod.
 * It defines the mod ID, mod name, and provides methods for creating info and error logs.
 */
public class Constants {
    public static final String MOD_ID = "matthiesen_lib";
    public static final String ModName = "Matthiesen Lib";
    public static Logger LOGGER = LogManager.getLogger(ModName);

    /**
     * Creates an info log with the specified message.
     */
    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    /**
     * Creates an error log with the specified message.
     */
    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }

    /**
     * Creates an error log with the specified message and throwable, including the full stack trace.
     */
    public static void createErrorLog(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }
}
