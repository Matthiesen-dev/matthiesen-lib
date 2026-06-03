package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.config.ApiConfig;

/**
 * A manager class for handling the API configuration. This class is responsible for loading, caching, and providing access to the API config instance.
 * It uses a ConfigManager to handle the actual loading and saving of the config file, and it ensures that the config is only loaded once and cached in
 * memory for efficient access. The modInitializer method should be called during the API initialization phase to load the config from disk, and the
 * getApiConfig method can be used to access the loaded config instance throughout the API. If the config file is modified, the reload method can be
 * called to load the new values into memory. This class is designed to be a static utility class, so all methods and fields are static, and the constructor
 * is private to prevent instantiation.
 */
public final class MatthiesenLibApiConfigManager {
    private static boolean initialized;
    private static final ConfigManager<ApiConfig> API_CONFIG_MANAGER =
            new ConfigManager<>(ApiConfig.class, "config");

    /**
     * Private constructor to prevent instantiation. This class is not meant to be instantiated, as it serves as a static manager
     * for the API configuration. All methods and fields are static, so there is no need for an instance of this class. By making
     * the constructor private, we ensure that no other classes can create an instance of MatthiesenLibApiConfigManager, which
     * enforces its intended usage as a static utility class.
     */
    private MatthiesenLibApiConfigManager() {}

    /**
     * Initializes the API configuration manager by loading the config from disk. This method should be called once during the API
     * initialization phase to ensure that the config is loaded and available for use by other parts of the API. If this method is called
     * multiple times, it will only load the config on the first call and will do nothing on subsequent calls to prevent unnecessary disk reads.
     */
    public static void modInitializer() {
        if (initialized) return;
        API_CONFIG_MANAGER.loadConfig();
        initialized = true;
    }

    /**
     * Reloads the API configuration from disk. This method should be called whenever the config file is modified to ensure that the latest values are loaded into memory.
     */
    public static void reload() {
        API_CONFIG_MANAGER.loadConfig();
    }

    /**
     * Gets the current API configuration. This method will return the loaded config instance, which is cached in memory after being loaded from disk.
     * @return the current API configuration instance. This instance is shared across the entire API and should not be modified directly by consumers. If you
     * need to change config values, you should modify the config file on disk and then call reload() to load the new values into memory.
     */
    public static ApiConfig getApiConfig() {
        return API_CONFIG_MANAGER.getConfig();
    }
}
