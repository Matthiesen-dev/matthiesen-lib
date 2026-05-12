package dev.matthiesen.common.matthiesen_lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class ConfigBase {
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
