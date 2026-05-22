package dev.matthiesen.common.matthiesen_lib.config;

/**
 * @deprecated Use {@link dev.matthiesen.api.matthiesen_lib_api.config.ConfigManager} instead.
 */
@Deprecated
@SuppressWarnings("unused")
public class ConfigManager<T> extends dev.matthiesen.api.matthiesen_lib_api.config.ConfigManager<T> {
    public ConfigManager(Class<T> configClass, String configName) {
        super(configClass, configName);
    }

    public ConfigManager(Class<T> configClass, String configName, String modId) {
        super(configClass, configName, modId);
    }
}
