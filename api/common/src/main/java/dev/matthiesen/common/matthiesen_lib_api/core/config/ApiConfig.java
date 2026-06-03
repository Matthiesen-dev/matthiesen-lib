package dev.matthiesen.common.matthiesen_lib_api.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * A base configuration class for the API, containing common settings that can be used across different modules.
 */
public final class ApiConfig {
    /**
     * Constructs a new ApiConfig instance with default values. This constructor is used by the GSON library when deserializing
     * the config from a JSON file. The default values for the fields are set in their declarations, so this constructor does not
     * need to initialize them. The GSON library will populate the fields with the values from the JSON file, or use the default values
     * if they are not present in the file.
     */
    public ApiConfig() {}

    /**
     * If true, the API will suppress most internal logging output. This is intended for use in cases where the API is being used in a context
     * where logging is not desired, such as in a mod that wants to handle its own logging or in a testing environment.
     */
    @SerializedName("suppressedLogging")
    public boolean suppressedLogging = false;

    /**
     * A shared GSON instance for all configs to use, with HTML escaping disabled and pretty printing enabled.
     */
    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
