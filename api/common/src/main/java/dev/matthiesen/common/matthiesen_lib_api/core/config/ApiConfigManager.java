package dev.matthiesen.common.matthiesen_lib_api.core.config;

import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;

/**
 * A configuration manager for API configurations. This class extends the base ConfigManager and is designed to manage configurations specific to the API module. It takes a configuration class and a configuration name as parameters, and it uses the mod ID defined in MatthiesenLibApiConstants to ensure that the configuration is properly namespaced. This allows for organized and modular management of API-related configurations within the broader configuration system of the mod.
 * @param <T> The type of the configuration class that this manager will handle. This should be a class that represents the structure of the configuration data, typically annotated for JSON serialization and deserialization.
 */
public final class ApiConfigManager<T> extends ConfigManager<T> {
    /**
     * Creates a new ApiConfigManager for the specified configuration class and name. This constructor initializes the ConfigManager with the provided configuration class and name, and it uses the mod ID from MatthiesenLibApiConstants to ensure that the configuration is stored in the correct namespace. This allows for proper organization of configuration files and prevents conflicts with other configurations that may have the same name but belong to different modules or features.
     * @param configClass The class of the configuration that this manager will handle. This should be a class that represents the structure of the configuration data, typically annotated for JSON serialization and deserialization.
     * @param configName The name of the configuration file (without the .json extension) that this manager will handle. This name is used to identify the configuration file on disk and should be unique within the mod's configuration namespace to avoid conflicts with other configurations.
     */
    public ApiConfigManager(Class<T> configClass, String configName) {
        super(configClass, configName, MatthiesenLibApiConstants.MOD_ID);
    }
}
