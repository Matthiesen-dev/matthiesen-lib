package dev.matthiesen.common.matthiesen_lib_api.core.metric.service;

import dev.matthiesen.libs.faststats.SdkInfo;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;

/**
 * Provides a custom user agent for the FastStats SDK, including the mod name and version.
 */
public final class UserAgentProvider {
    /**
     * Utility class to provide a custom user agent for the FastStats SDK, including the mod name and version.
     */
    public UserAgentProvider() {}

    /**
     * Custom user agent provider for FastStats SDK, providing the mod name and version.
     */
    public static final class MatthiesenLibUniversalAgent implements SdkInfo.UserAgentProvider {
        /**
         * Creates a new instance of the MatthiesenLibUniversalAgent.
         */
        public MatthiesenLibUniversalAgent() {}

        @Override
        public String getUserAgent(SdkInfo sdkInfo) {
            return "Matthiesen Lib API Metrics " + sdkInfo.getName() + "/" + getVersion() + " https://mods.matthiesen.dev/matthiesen-lib-api/";
        }

        /**
         * Gets the version of the mod from the mod container.
         * @return The version of the mod.
         */
        private String getVersion() {
            return MatthiesenLibApi.getModContainer(MatthiesenLibApiConstants.MOD_ID).getModVersion();
        }
    }
}
