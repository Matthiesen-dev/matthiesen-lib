package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.config.ApiConfig;

public final class MatthiesenLibApiConfigManager {
    private static boolean initialized;
    private static final ConfigManager<ApiConfig> API_CONFIG_MANAGER =
            new ConfigManager<>(ApiConfig.class, "config");

    private MatthiesenLibApiConfigManager() {}

    public static void modInitializer() {
        if (initialized) return;
        API_CONFIG_MANAGER.loadConfig();
        initialized = true;
    }

    public static void reload() {
        API_CONFIG_MANAGER.loadConfig();
    }

    public static ApiConfig getApiConfig() {
        return API_CONFIG_MANAGER.getConfig();
    }
}
