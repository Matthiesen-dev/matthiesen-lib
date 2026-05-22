package dev.matthiesen.api.matthiesen_lib.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains constants and logging utilities for the Matthiesen Lib API.
 * It defines the mod ID, mod name, and provides methods for creating info and error logs.
 * The logger can be overridden at runtime for consumers that want custom logging behavior.
 */
public final class MatthiesenLibConstants {
    /**
     * The unique identifier for the Matthiesen Lib mod. This constant is used for registration and identification purposes
     * throughout the mod, ensuring that all components of the mod are correctly associated with this mod ID.
     */
    public static final String MOD_ID = "matthiesen_lib";

    /**
     * The name of the mod, used for logging and identification purposes. This constant is used as the logger name when
     * initializing the LOGGER instance, allowing for organized logging specific to this mod.
     */
    public static final String MOD_NAME = "Matthiesen Lib";

    /**
     * The logger instance for the Matthiesen Lib API. This logger is used to create log messages for the API, including
     * info and error logs. The logger is initialized using LogManager.getLogger with the mod name as the logger name, allowing
     * for organized logging specific to this API.
     */
    private static volatile Logger logger = LogManager.getLogger(MOD_NAME);

    /**
     * Default constructor for the Constants class. This constructor is private to prevent instantiation of this utility class,
     * as all members are static and there is no need to create an instance of this class.
     */
    private MatthiesenLibConstants() {
    }

    /**
     * Gets the current logger instance used by the API.
     * @return the current logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Replaces the current logger instance with a custom logger.
     * @param newLogger the new logger to use; ignored if null.
     */
    public static void setLogger(Logger newLogger) {
        if (newLogger != null) {
            logger = newLogger;
        }
    }

    /**
     * Rebinds the current logger using a new logger name.
     * @param loggerName the logger name to use; ignored if null or blank.
     */
    public static void setLoggerName(String loggerName) {
        if (loggerName != null && !loggerName.isBlank()) {
            logger = LogManager.getLogger(loggerName);
        }
    }

    /**
     * Creates an info log with the specified message.
     * @param message The message to log as info.
     */
    public static void createInfoLog(String message) {
        logger.info(message);
    }

    /**
     * Creates an error log with the specified message.
     * @param message The message to log as an error. This will be logged at the error level without any associated throwable or stack trace.
     */
    public static void createErrorLog(String message) {
        logger.error(message);
    }

    /**
     * Creates an error log with the specified message and throwable, including the full stack trace.
     * @param message The message to log as an error. This will be logged at the error level along with the stack trace of the provided throwable.
     * @param throwable The throwable whose stack trace will be included in the log. This allows for detailed error logging,
     *                  including the context of the error and the stack trace for debugging purposes.
     */
    public static void createErrorLog(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}

