package dev.matthiesen.common.matthiesen_lib.core;

import org.apache.logging.log4j.Logger;

/**
 * This class contains constants and logging utilities for the Matthiesen Lib mod.
 * It defines the mod ID, mod name, and provides methods for creating info and error logs.
 */
@SuppressWarnings("unused")
public final class MatthiesenLibConstants {
    /**
     * Default constructor for the Constants class. This constructor is private to prevent instantiation of this utility class,
     * as all members are static and there is no need to create an instance of this class.
     */
    private MatthiesenLibConstants() {}

    /**
     * The unique identifier for the Matthiesen Lib mod. This constant is used for registration and identification purposes
     * throughout the mod, ensuring that all components of the mod are correctly associated with this mod ID.
     */
    public static final String MOD_ID = dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.MOD_ID;

    /**
     * The name of the mod, used for logging and identification purposes. This constant is used as the logger name when
     * initializing the LOGGER instance, allowing for organized logging specific to this mod.
     */
    public static final String ModName = dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.MOD_NAME;

    /**
     * The logger instance for the Matthiesen Lib mod. This logger is used to create log messages for the mod, including
     * info and error logs. The logger is initialized using LogManager.getLogger with the mod name as the logger name, allowing
     * for organized logging specific to this mod.
     */
    public static Logger LOGGER = dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.getLogger();

    public static void setLogger(Logger logger) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.setLogger(logger);
        LOGGER = dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.getLogger();
    }

    public static void setLoggerName(String loggerName) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.setLoggerName(loggerName);
        LOGGER = dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.getLogger();
    }

    /**
     * Creates an info log with the specified message.
     * @param message The message to log as info.
     */
    public static void createInfoLog(String message) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.createInfoLog(message);
    }

    /**
     * Creates an error log with the specified message.
     * @param message The message to log as an error. This will be logged at the error level without any associated throwable or stack trace.
     */
    public static void createErrorLog(String message) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.createErrorLog(message);
    }

    /**
     * Creates an error log with the specified message and throwable, including the full stack trace.
     * @param message The message to log as an error. This will be logged at the error level along with the stack trace of the provided throwable.
     * @param throwable The throwable whose stack trace will be included in the log. This allows for detailed error logging,
     *                  including the context of the error and the stack trace for debugging purposes.
     */
    public static void createErrorLog(String message, Throwable throwable) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants.createErrorLog(message, throwable);
    }
}
