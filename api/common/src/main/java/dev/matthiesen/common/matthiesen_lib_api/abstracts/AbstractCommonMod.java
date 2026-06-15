package dev.matthiesen.common.matthiesen_lib_api.abstracts;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.*;
import dev.matthiesen.common.matthiesen_lib_api.core.metric.UniversalMetricContext;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibPlatform;
import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Token;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Base class for common mods to extend, providing utility methods for registering with the API.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final ErrorTracker ERROR_TRACKER = MatthiesenLibApiMetricsManager.getErrorTracker();
    private MatthiesenLibApi.RegistryBuilder registryBuilder;
    private UniversalMetricContext METRIC_CONTEXT;

    /**
     * The Metric's Token from FastStats.dev for the Mod/Plugin
     * @return The Token as a String, or null if Metrics are not used
     */
    public abstract @Nullable @Token String getMetricsToken();

    /**
     * The reload task to run when the server reloads datapacks, or when the /reload command is used.
     * @return A Runnable containing the code to run on reload, or null if no task is needed
     */
    public abstract Runnable reload();

    /**
     * The method to register the mod's configuration with the API.
     * @param MOD_ID The mod id of the mod registering
     * @param MOD_NAME The mod name of the mod registering
     */
    public AbstractCommonMod(String MOD_ID, String MOD_NAME) {
        this.MOD_ID = MOD_ID;
        this.MOD_NAME = MOD_NAME;
        var metricToken = getMetricsToken();
        if (metricToken != null) {
            METRIC_CONTEXT = MatthiesenLibApiMetricsManager.makeErrorMetricsContext(
                    MOD_ID,
                    metricToken,
                    ERROR_TRACKER
            );
        }
        LOGGER = LogManager.getLogger(MOD_NAME);
    }

    /**
     * Registers the mod's configuration with the API.
     * @param MOD_ID The mod id of the mod registering
     */
    public AbstractCommonMod(String MOD_ID) {
        this(MOD_ID, MOD_ID);
    }

    /**
     * Initializes the mod. This should be called in the mod's main class during initialization.
     */
    public void initialize() {
        this.registryBuilder = new MatthiesenLibApi.RegistryBuilder(MOD_ID);
        MatthiesenLibApi.registerModToApiMetrics(MOD_ID);
        MatthiesenLibApi.registerReloadRunnable(MOD_ID, reload());
    }

    /**
     * Get the mod's ID
     * @return The mod's ID
     */
    public String getModId() {
        return MOD_ID;
    }

    /**
     * Get the mod's name
     * @return The mod's name
     */
    public String getModName() {
        return MOD_NAME;
    }

    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * Track an error that occurred in the mod. This will be sent to the metrics system if a metrics token is provided.
     * @param throwable The error to track
     */
    public void trackError(Throwable throwable) {
        ERROR_TRACKER.trackError(throwable);
    }

    /**
     * Gets the Metrics Context for this mod. Will be null if no metrics token was provided.
     * @return The Metrics Context for this mod, or null if no metrics token was provided.
     */
    public UniversalMetricContext getMetricContext() {
        return METRIC_CONTEXT;
    }

    // ---- Utils ----

    /**
     * Send an info log message using the mod's logger
     * @param message The message to log
     */
    public void createInfoLog(String message) {
        getLogger().info(message);
    }

    /**
     * Send an error log message using the mod's logger, and track the error with the metrics system if a metrics token is provided
     * @param message The message to log
     * @param throwable The error to log and track
     */
    public void createErrorLog(String message, Throwable throwable) {
        trackError(throwable);
        getLogger().error(message, throwable);
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public MinecraftServer getMinecraftServer() {
        return MatthiesenLibApi.getMinecraftServer();
    }

    /**
     * Checks if a mod with the given mod ID is loaded using the platform-specific implementation provided by the CommonPlatform service.
     * @param modId The mod ID to check for (e.g., "minecraft", "fabric", "forge")
     * @return true if the mod is loaded, false otherwise
     */
    public boolean isModLoaded(String modId) {
        return MatthiesenLibApi.isModLoaded(modId);
    }

    /**
     * Checks if the current environment is a development environment using the platform-specific implementation provided by the CommonPlatform service.
     * @return true if the current environment is a development environment, false otherwise
     */
    public boolean isDevelopmentEnvironment() {
        return MatthiesenLibApi.isDevelopmentEnvironment();
    }

    /**
     * Get the mod container for a mod with the given mod ID. This method can be used to access information about a loaded mod, such as its metadata, resources,
     * or other properties. The implementation of this method may vary depending on the mod loader, but it generally retrieves the mod container from the list
     * of loaded mods based on the specified mod ID.
     * @param modId The mod ID to get the mod container for. This should be the unique identifier of the mod, which is typically defined in the mod's metadata
     *              and used for registration and integration purposes.
     * @return The mod container for the mod with the given mod ID, or null if no such mod is loaded. The mod container provides access to various properties
     * and information about the mod, allowing you to interact with it in a more detailed way if needed.
     */
    public MatthiesenLibModContainer getModContainer(String modId) {
        return MatthiesenLibApi.getModContainer(modId);
    }

    /**
     * Get the configuration file path for the mod. This method can be used to access the configuration file for the mod, allowing you to read and write configuration
     * @param dir The directory where the configuration file is located. This should be a string that specifies the path to the directory, which must be relative to the game's /config/`dir` directory. The method will combine this directory path with the file name provided in the 'file' parameter to construct the full path to the configuration file for the mod. This allows you to organize your mod's configuration files in a specific subdirectory within the main config directory, helping to keep things organized and preventing conflicts with other mods' configuration files.
     * @param file The name of the configuration file. This should be a string that specifies the name of the file, including the file extension (e.g., "config.json"). The method will combine this file name with the directory path provided in the 'dir' parameter to construct the full path to the configuration file, which can then be accessed for reading or writing configuration data for the mod.
     * @return The Path object representing the full path to the configuration file for the mod. This allows you to access the configuration file in a way that is compatible with the underlying file system and the specific mod loader's conventions for storing configuration files. You can use this Path object to read from or write to the configuration file as needed for your mod's functionality.
     */
    public Path getModConfig(String dir, String file) {
        return MatthiesenLibApi.getModConfig(dir, file);
    }

    /**
     * Get the current environment type (e.g., client, server, or dedicated server) using the platform-specific implementation provided by the CommonPlatform service. This method allows you to determine the current environment in which the mod is running, which can be useful for conditionally executing code that should only run on certain sides (e.g., client-only code or server-only code).
     * @return The current environment type, represented as a value from the MatthiesenLibPlatform.ENVIRONMENT enum. This value indicates whether the mod is running in a client environment, a server environment, or a dedicated server environment, allowing you to make informed decisions about which code to execute based on the current environment context.
     */
    public MatthiesenLibPlatform.ENVIRONMENT getEnvironmentType() {
        return MatthiesenLibApi.getEnvironmentType();
    }

    /**
     * Registers a config manager for the mod. This should be called in the initialize method of the mod.
     * @param configClass The class of the config.
     * @param fileName The name of the config file (without extension).
     * @return The config manager instance.
     * @param <T> The type of the config.
     */
    public <T> ConfigManager<T> createConfigManager(Class<T> configClass, String fileName) {
        return new ConfigManager<>(configClass, fileName, MOD_ID);
    }

    /**
     * Registers a command with the MatthiesenLib command system. This method can be called at any time, and the command will be registered appropriately based on the state of the command registrar.
     * @param command The AbstractCommand to register. This command will be added to the MatthiesenLib command system and will be available for use once the registrar is ready.
     */
    public void registerCommand(AbstractCommand command) {
        MatthiesenLibApi.registerCommand(command);
    }

    /**
     * Gets the registry builder for this mod. This is used to register various components to the API.
     * @return The registry builder for this mod.
     */
    public MatthiesenLibApi.RegistryBuilder getRegistryBuilder() {
        return registryBuilder;
    }

    /**
     * Registers a text parser. This method is thread-safe and can be called at any time. If a parser with the same type is already registered, it will be overwritten.
     * @param parser The text parser to register. The parser's type is determined by its getType() method, and it will be initialized before being added to the registry.
     */
    public void registerTextParser(MatthiesenLibTextParser parser) {
        MatthiesenLibApi.registerTextParser(parser);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla parser is returned as a fallback.
     * @param type The type of the text parser to retrieve. This should match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public MatthiesenLibTextParser getTextParser(String type) {
        return MatthiesenLibApi.getTextParser(type);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla
     * parser is returned as a fallback.
     * @param type The type of the text parser to retrieve, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public MatthiesenLibTextParser getTextParser(MatthiesenLibBuiltInTextParsers type) {
        return MatthiesenLibApi.getTextParser(type);
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param handler the implementation of the MatthiesenLibPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     */
    public void registerPlayerEventHandler(MatthiesenLibPlayerEventHandler handler) {
        MatthiesenLibApi.registerPlayerEventHandler(MOD_ID, handler);
    }

    /**
     * Registers a server event handler for a specific mod. This method allows mods to register their own implementations of the IServerEventHandler interface,
     * enabling them to receive callbacks for server events such as starting, ticking, and stopping. By registering a server event handler, mods can define custom logic to be executed in response to these events, allowing for enhanced functionality and integration with the server lifecycle.
     * @param handler the implementation of the MatthiesenLibServerEventHandler interface that will handle server events for the specified mod. This parameter allows mods to define their own logic for handling server start, tick, and stop events, enabling custom behavior in response to these events.
     */
    public void registerServerEventHandler(MatthiesenLibServerEventHandler handler) {
        MatthiesenLibApi.registerServerEventHandler(MOD_ID, handler);
    }
}
