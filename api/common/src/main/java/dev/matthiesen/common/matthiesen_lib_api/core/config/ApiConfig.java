package dev.matthiesen.common.matthiesen_lib_api.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public final class ApiConfig {
    @SerializedName("suppressedLogging")
    public boolean suppressedLogging = false;

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
