package dev.matthiesen.common.matthiesen_lib_api.core.metric.service;

import dev.faststats.SdkInfo;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import org.jspecify.annotations.NonNull;

public final class UserAgentProvider {
    public static final class MatthiesenLibUniversalAgent implements SdkInfo.UserAgentProvider {
        @Override
        public @NonNull String getUserAgent(@NonNull SdkInfo sdkInfo) {
            return "Matthiesen Lib API Metrics " + sdkInfo.getName() + "/" + getVersion() + " https://mods.matthiesen.dev/matthiesen-lib-api/";
        }

        private String getVersion() {
            return MatthiesenLibApi.getModContainer(MatthiesenLibApiConstants.MOD_ID).getModVersion();
        }
    }
}
