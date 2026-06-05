package dev.matthiesen.common.matthiesen_lib_api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;

/**
 * A generic configuration manager for handling JSON-based config files.
 * It supports loading, saving, and merging default values with existing config files.
 * @param <T> The type of the config class to manage
 */
@SuppressWarnings("unused")
public class ConfigManager<T> {
    private final Class<T> configClass;
    private final String configName;
    private final String modId;
    private final Gson gson;
    private T config;

    /**
     * Creates a new ConfigManager for the specified config class and name.
     *
     * @param configClass The class of the config to manage
     * @param configName The name of the config file (without .json extension)
     * @deprecated Use the constructor that includes the modId parameter to specify the config folder namespace. This will default to using the API's mod id, which may not be appropriate for all configs and can lead to conflicts if multiple configs use the same name. By specifying a mod id namespace, you can ensure that your config is stored in a unique folder and avoid potential conflicts with other configs.
     */
    @Deprecated(forRemoval = true)
    public ConfigManager(Class<T> configClass, String configName) {
        this(configClass, configName, MatthiesenLibApiConstants.MOD_ID);
    }

    /**
     * Creates a new ConfigManager for the specified config class, name, and mod id namespace.
     *
     * @param configClass The class of the config to manage
     * @param configName The name of the config file (without .json extension)
     * @param modId The mod id namespace used for the config folder
     */
    public ConfigManager(Class<T> configClass, String configName, String modId) {
        this.configClass = configClass;
        this.configName = configName;
        this.modId = modId;
        this.gson = getGsonFromConfigClass();
    }

    /**
     * Attempts to get the GSON instance from the config class.
     * Falls back to creating a new Gson instance if not available.
     */
    private Gson getGsonFromConfigClass() {
        try {
            Field gsonField = configClass.getDeclaredField("GSON");
            gsonField.setAccessible(true);
            return (Gson) gsonField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            MatthiesenLibApiConstants.createDebugLog("No GSON field found in " + configClass.getSimpleName() + ", using default Gson instance");
            return new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();
        }
    }

    /**
     * Creates a new instance of the config class.
     */
    private T createDefaultConfig() {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            MatthiesenLibApi.ERROR_TRACKER.trackError(e);
            MatthiesenLibApiConstants.createErrorLog("Failed to create default instance of " + configClass.getSimpleName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the config from the file system. If the config file does not exist, it will create a new one with default values.
     * @return The loaded config instance
     */
    public T loadConfig() {
        String configFileLoc = getConfigFileLocation();
        MatthiesenLibApiConstants.createDebugLog("Loading config file found at: " + configFileLoc);
        File configFile = new File(configFileLoc);
        boolean madeDir = configFile.getParentFile().mkdirs();

        if (madeDir) {
            MatthiesenLibApiConstants.createDebugLog("Config Directory exists");
        }

        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile)) {
                T defaultConfig = createDefaultConfig();
                String defaultConfigJson = gson.toJson(defaultConfig);

                JsonElement fileConfigElement = JsonParser.parseReader(fileReader);
                JsonElement defaultConfigElement = JsonParser.parseString(defaultConfigJson);

                JsonElement mergedConfigElement = mergeConfigs(
                        defaultConfigElement.getAsJsonObject(),
                        fileConfigElement.getAsJsonObject()
                );

                config = gson.fromJson(mergedConfigElement, configClass);
            } catch (Exception e) {
                MatthiesenLibApiConstants.createErrorLog("Failed to load the config! Using default config as fallback", e);
                config = createDefaultConfig();
            }
        } else {
            config = createDefaultConfig();
        }

        saveConfig();
        return config;
    }

    /**
     * Merges the default config with the file config. If a key is missing in the file config, it will be added from the default config.
     * If a key is present in both configs and is a nested object, it will recursively merge them.
     * @param defaultConfig The default config as a JsonObject
     * @param fileConfig The file config as a JsonObject
     * @return The merged config as a JsonElement
     */
    private JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig) {
        MatthiesenLibApiConstants.createDebugLog("Checking for config merge.");
        boolean merged = false;

        for (String key : defaultConfig.keySet()) {
            if (!fileConfig.has(key)) {
                fileConfig.add(key, defaultConfig.get(key));
                MatthiesenLibApiConstants.createDebugLog(key + " not found in file config, adding from default.");
                merged = true;
            } else if (defaultConfig.get(key).isJsonObject() && fileConfig.get(key).isJsonObject()) {
                mergeConfigs(defaultConfig.getAsJsonObject(key), fileConfig.getAsJsonObject(key));
            }
        }

        if (merged) {
            MatthiesenLibApiConstants.createDebugLog("Successfully merged config.");
        }

        return fileConfig;
    }

    /**
     * Saves the current config to the file system. If the config is null, it will not save and log an error.
     */
    public void saveConfig() {
        try {
            String configFileLoc = getConfigFileLocation();
            MatthiesenLibApiConstants.createDebugLog("Saving config to: " + configFileLoc);
            File configFile = new File(configFileLoc);
            try (FileWriter fileWriter = new FileWriter(configFile)) {
                gson.toJson(config, fileWriter);
                fileWriter.flush();
            }
        } catch (Exception e) {
            MatthiesenLibApi.ERROR_TRACKER.trackError(e);
            MatthiesenLibApiConstants.createErrorLog("Failed to save config", e);
        }
    }

    /**
     * Gets the current config. If the config is null, it will attempt to load it from the file system.
     * @return The current config instance
     */
    public T getConfig() {
        if (config == null) {
            return loadConfig();
        }
        return config;
    }

    /**
     * Sets the current config. This will not automatically save the config to the file system, so you should call saveConfig()
     * after setting the config if you want to persist it.
     *
     * @param config The new config to set
     */
    public void setConfig(T config) {
        this.config = config;
    }

    private String getConfigFileLocation() {
        return System.getProperty("user.dir")
                + File.separator
                + "config"
                + File.separator
                + modId
                + File.separator
                + configName
                + ".json";
    }
}

