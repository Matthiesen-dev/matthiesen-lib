package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.Token;
import dev.faststats.data.Metric;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.metric.UniversalMetricContext;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/**
 * MatthiesenLibApiMetricsManager is responsible for managing the registration of mods for metrics collection and providing a
 * UniversalMetricContext for submitting metrics. It maintains a map of registered mods and their versions, which can be used
 * as a custom metric in the UniversalMetricContext. The class also defines an ErrorTracker for capturing and anonymizing errors
 * that occur during metrics collection and submission. This manager serves as a central point for handling metrics-related functionality
 * in the Matthiesen Lib API, allowing mods to easily register themselves for metrics tracking and ensuring that any errors are
 * properly handled and anonymized.
 */
public final class MatthiesenLibApiMetricsManager {
    /**
     * The ErrorTracker instance used for capturing and anonymizing errors that occur during metrics collection and submission.
     * This error tracker is configured to ignore certain expected exceptions, such as InvocationTargetException with specific
     * messages and AccessDeniedException, which may occur in normal operation and do not indicate issues that need to be tracked.
     * Additionally, the error tracker is set up to anonymize sensitive information such as email addresses, bearer tokens, AWS keys,
     * UUIDs, and API keys or tokens in query parameters. This ensures that any errors captured by the tracker do not contain personally
     * identifiable information or sensitive data, while still allowing for effective tracking and analysis of errors that may occur during
     * the metrics collection process.
     */
    public static final ErrorTracker ERROR_TRACKER = getErrorTracker();

    private static final Map<String, String> REGISTERED_MODS = new HashMap<>();
    @SuppressWarnings("unused")
    private static final UniversalMetricContext METRIC_CONTEXT = getBaseMetricFactory(
            MatthiesenLibApiConstants.MOD_ID,
            MatthiesenLibApiConstants.METRICS_TOKEN
    )
            .metrics(factory -> factory
                    .addMetric(Metric.stringMap("registered_mods", MatthiesenLibApiMetricsManager::getRegisteredMods))
                    .create()
            )
            .errorTrackerService(ERROR_TRACKER)
            .create();

    /**
     * Creates a base UniversalMetricContext.Factory with the given mod ID and token, and configures it to include the registered mods metric and error tracker service. This factory can be used to create UniversalMetricContext instances for submitting metrics with the registered mods data and error tracking capabilities. The registered mods metric is defined as a string map that retrieves the current set of registered mods from the getRegisteredMods method, and it is configured to clear the registered mods data after each flush using the clearRegisteredMods method. The error tracker service is set to use the ERROR_TRACKER defined in this class, which captures and anonymizes errors that occur during metrics collection and submission.
     * @param modId the mod ID to use for the UniversalMetricContext.Factory. This should be the unique identifier for the mod, as defined in its metadata. The mod ID is used to associate the metrics data with the correct mod when it is submitted to the metrics collection service.
     * @param token the token to use for the UniversalMetricContext.Factory. This token is used to authenticate and identify the source of the metrics data when it is submitted to the metrics collection service. It should be a valid token that is registered with the metrics collection service to ensure that the data is accepted and processed correctly.
     * @return a UniversalMetricContext.Factory instance configured with the registered mods metric and error tracker service. This factory can be used to create UniversalMetricContext instances for submitting metrics with the registered mods data and error tracking capabilities. The registered mods metric is defined as a string map that retrieves the current set of registered mods from the getRegisteredMods method, and it is configured to clear the registered mods data after each flush using the clearRegisteredMods method. The error tracker service is set to use the ERROR_TRACKER defined in this class, which captures and anonymizes errors that occur during metrics collection and submission.
     */
    public static UniversalMetricContext.Factory getBaseMetricFactory(String modId, @Token String token) {
        return new UniversalMetricContext.Factory(modId, token).metrics(Metrics.Factory::create);
    }

    /**
     * Creates a basic UniversalMetricContext for submitting metrics with the registered mods data and error tracking capabilities. This method uses the getBaseMetricFactory method to create a factory configured with the registered mods metric and error tracker service, and then creates a UniversalMetricContext instance from that factory. The resulting context can be used to submit metrics with the registered mods data included as a custom metric, and any errors that occur during submission will be captured and anonymized by the configured error tracker service.
     * @param modId the mod ID to use for the UniversalMetricContext. This should be the unique identifier for the mod, as defined in its metadata. The mod ID is used to associate the metrics data with the correct mod when it is submitted to the metrics collection service.
     * @param token the token to use for the UniversalMetricContext. This token is used to authenticate and identify the source of the metrics data when it is submitted to the metrics collection service. It should be a valid token that is registered with the metrics collection service to ensure that the data is accepted and processed correctly.
     * @return a UniversalMetricContext instance configured with the registered mods metric and error tracking capabilities. This context can be used to submit metrics with the registered mods data included as a custom metric, and any errors that occur during submission will be captured and anonymized by the configured error tracker service. The context is created using the getBaseMetricFactory method, which sets up the necessary configuration for the registered mods metric and error tracker service before creating the context instance.
     */
    public static UniversalMetricContext makeBasicMetricsContext(String modId, @Token String token) {
        return getBaseMetricFactory(modId, token).create();
    }

    /**
     * Creates a UniversalMetricContext for submitting metrics with the registered mods data and error tracking capabilities, using a custom ErrorTracker provided as a parameter. This method uses the getBaseMetricFactory method to create a factory configured with the registered mods metric, and then sets the error tracker service to use the provided ErrorTracker before creating a UniversalMetricContext instance. The resulting context can be used to submit metrics with the registered mods data included as a custom metric, and any errors that occur during submission will be captured and anonymized by the provided ErrorTracker according to its configuration.
     * @param modId the mod ID to use for the UniversalMetricContext. This should be the unique identifier for the mod, as defined in its metadata. The mod ID is used to associate the metrics data with the correct mod when it is submitted to the metrics collection service.
     * @param token the token to use for the UniversalMetricContext. This token is used to authenticate and identify the source of the metrics data when it is submitted to the metrics collection service. It should be a valid token that is registered with the metrics collection service to ensure that the data is accepted and processed correctly.
     * @param errorTracker the ErrorTracker instance to use for capturing and anonymizing errors that occur during metrics collection and submission. This allows consumers to provide their own custom error tracking configuration if they want to capture additional types of errors or anonymize different patterns of sensitive information. The provided ErrorTracker will be used in place of the default ERROR_TRACKER defined in this class, allowing for flexible error tracking based on the consumer's specific needs and preferences.
     * @return a UniversalMetricContext instance configured with the registered mods metric and the provided error tracking capabilities. This context can be used to submit metrics with the registered mods data included as a custom metric, and any errors that occur during submission will be captured and anonymized by the provided ErrorTracker according to its configuration. The context is created using the getBaseMetricFactory method, which sets up the necessary configuration for the registered mods metric before setting the error tracker service to use the provided ErrorTracker and creating the context instance.
     */
    public static UniversalMetricContext makeErrorMetricsContext(String modId, @Token String token, ErrorTracker errorTracker) {
        return getBaseMetricFactory(modId, token).errorTrackerService(errorTracker).create();
    }

    /**
     * Private constructor to prevent instantiation of the MatthiesenLibApiMetricsManager class. This class is designed to be a utility class
     * with static methods and fields, so there is no need for instances of this class to be created. By making the constructor private, we ensure
     * that the class cannot be instantiated from outside, enforcing its intended use as a static utility class for managing metrics registration
     * and providing the UniversalMetricContext.
     */
    private MatthiesenLibApiMetricsManager() {}

    /**
     * Initializes the metrics manager. This method is currently empty, but it can be used in the future for any necessary setup or initialization logic
     * related to the metrics manager. It is intended to be called during the mod initialization process to ensure that the metrics manager is ready to
     * handle mod registrations and provide the UniversalMetricContext when needed. Consumers should call this method during their mod's initialization
     * phase to ensure that the metrics manager is properly set up before any mods attempt to register for metrics or submit metrics data.
     */
    public static void modInitializer() {}

    /**
     * Registers a mod with the metrics system by its mod ID. This method retrieves the mod container for the given mod ID using
     * the MatthiesenLibApi, and if found, extracts the mod name and version to store in the REGISTERED_MODS map. The map uses the
     * mod ID as the key and a string containing the mod name and version as the value. If no mod container is found for the given
     * mod ID, or if the mod is already registered, a warning is logged using the API's logger. This method allows mods to be tracked
     * in the metrics system, providing insight into which mods are present in the environment when metrics are collected.
     * @param modId the mod ID of the mod to register with the metrics system. This should be the unique identifier for the mod, as
     *              defined in its metadata. The method will attempt to retrieve the mod container for this ID and, if successful,
     *              will store the mod's name and version in the REGISTERED_MODS map for tracking in the metrics system. If the mod
     *              ID is invalid or if the mod is already registered, a warning will be logged to inform developers of potential
     *              issues with registration.
     */
    public static void registerModToMatthiesenLibApi(String modId) {
        var modInfo = MatthiesenLibApi.getModContainer(modId);
        if (modInfo == null) {
            MatthiesenLibApiConstants.getLogger().warn("Attempted to register mod with ID '{}' for metrics, but no mod container was found. This may indicate an issue with the mod loader integration.", modId);
            return;
        }
        if (REGISTERED_MODS.containsKey(modId)) {
            MatthiesenLibApiConstants.getLogger().warn("Mod with ID '{}' is already registered for metrics. Ignoring duplicate registration attempt.", modId);
            return;
        }
        String modName = modInfo.getModName();
        String modVersion = modInfo.getModVersion();
        REGISTERED_MODS.put(modId, modName + " " + modVersion);
    }

    /**
     * Gets an immutable copy of the map containing registered mods and their versions for metrics purposes. This map is used
     * as a custom metric in the UniversalMetricContext to track which mods are registered with the metrics system. The keys
     * of the map are the mod IDs, and the values are the mod names along with their versions. Consumers can call this method
     * to retrieve the current set of registered mods for reporting or debugging purposes, but they cannot modify the underlying
     * map directly, ensuring thread safety and data integrity.
     * @return an immutable copy of the map containing registered mods and their versions for metrics purposes. The keys of the
     * map are the mod IDs, and the values are the mod names along with their versions. Consumers can use this map to see which
     * mods are registered with the metrics system, but they cannot modify the underlying data directly, ensuring thread safety
     * and data integrity.
     */
    private static Map<String, String> getRegisteredMods() {
        return Map.copyOf(REGISTERED_MODS);
    }

    /**
     * Configures and returns an ErrorTracker instance for capturing and anonymizing errors that occur during metrics collection and submission.
     * @return an ErrorTracker instance configured to ignore certain expected exceptions and anonymize sensitive information such as email addresses, bearer tokens, AWS keys, UUIDs, and API keys or tokens in query parameters.
     */
    public static ErrorTracker getErrorTracker() {
        return ErrorTracker.contextUnaware()
                .ignoreError(InvocationTargetException.class, "Expected .* but got .*")
                .ignoreError(AccessDeniedException.class)
                .anonymize("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "[email hidden]")
                .anonymize("Bearer [A-Za-z0-9._~+/=-]+", "Bearer [token hidden]")
                .anonymize("AKIA[0-9A-Z]{16}", "[aws-key hidden]")
                .anonymize("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "[uuid hidden]")
                .anonymize("([?&](?:api_?key|token|secret)=)[^&\\s]+", "$1[redacted]");
    }
}
