package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.libs.faststats.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains constants and logging utilities for the Matthiesen Lib API.
 * It defines the mod ID, mod name, and provides methods for creating info and error logs.
 * The logger can be overridden at runtime for consumers that want custom logging behavior.
 */
public final class MatthiesenLibApiConstants {
    /**
     * The unique identifier for the Matthiesen Lib mod. This constant is used for registration and identification purposes
     * throughout the mod, ensuring that all components of the mod are correctly associated with this mod ID.
     */
    public static final String MOD_ID = "matthiesen_lib_api";

    /**
     * The name of the mod, used for logging and identification purposes. This constant is used as the logger name when
     * initializing the LOGGER instance, allowing for organized logging specific to this mod.
     */
    public static final String MOD_NAME = "Matthiesen Lib API";

    /**
     * A token used for metrics collection. The token is used to authenticate and identify the source of the metrics data when it
     * is submitted to the metrics collection service.
     */
    public static @Token final String METRICS_TOKEN = "40d72b3b79407e5d372d5790b7dee654";

    /**
     * The logger instance for the Matthiesen Lib API. This logger is used to create log messages for the API, including
     * info and error logs. The logger is initialized using LogManager.getLogger with the mod name as the logger name, allowing
     * for organized logging specific to this API.
     */
    private static final Logger logger = LogManager.getLogger(MOD_NAME);

    /**
     * Default constructor for the Constants class. This constructor is private to prevent instantiation of this utility class,
     * as all members are static and there is no need to create an instance of this class.
     */
    private MatthiesenLibApiConstants() {}

    /**
     * Gets the current logger instance used by the API.
     * @return the current logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Creates an info log with the specified message.
     * @param message The message to log as info.
     */
    public static void createInfoLog(String message) {
        logger.info(message);
    }

    /**
     * Creates a debug log with the specified message. This method is intended for logging detailed information that may be useful for debugging purposes, and will only be logged if the logger is configured to include debug level messages.
     * @param message The message to log as debug. This will be logged at the debug level, which is typically used for detailed information that may be useful for debugging but is not necessary for regular operation. The actual logging of this message will depend on the logger's configuration and whether debug level logging is enabled.
     */
    public static void createDebugLog(String message) {
        logger.debug(message);
    }

    /**
     * Creates an extended info log with the specified message. This method checks the API configuration to determine if logging is suppressed,
     * @param message The message to log as info. This will be logged at the info level if logging is not suppressed in the API configuration.
     *                If logging is suppressed, this method will return without logging the message, allowing for dynamic control over logging
     *                behavior based on the API configuration settings.
     */
    public static void createExtendedLog(String message) {
        boolean cannotContinue = MatthiesenLibApiConfigManager.getApiConfig().suppressedLogging;
        if (cannotContinue) return;
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

