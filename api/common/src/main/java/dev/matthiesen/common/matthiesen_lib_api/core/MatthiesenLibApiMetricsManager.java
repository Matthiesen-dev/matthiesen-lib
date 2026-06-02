package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
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
    private static final Map<String, String> REGISTERED_MODS = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of the MatthiesenLibApiMetricsManager class. This class is designed to be a utility class
     * with static methods and fields, so there is no need for instances of this class to be created. By making the constructor private, we ensure
     * that the class cannot be instantiated from outside, enforcing its intended use as a static utility class for managing metrics registration
     * and providing the UniversalMetricContext.
     */
    private MatthiesenLibApiMetricsManager() {}

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
    public static void registerMod(String modId) {
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
    public static Map<String, String> getRegisteredMods() {
        return Map.copyOf(REGISTERED_MODS);
    }

    private static final UniversalMetricContext metricContext = new UniversalMetricContext.Factory(
            MatthiesenLibApiConstants.MOD_ID,
            MatthiesenLibApiConstants.METRICS_TOKEN
    )
            .metrics(Metrics.Factory::create)
            .metrics(factory -> factory
                    .addMetric(Metric.stringMap("registered_mods", MatthiesenLibApiMetricsManager::getRegisteredMods))
                    .create()
            )
            .errorTrackerService(MatthiesenLibApi.ERROR_TRACKER)
            .create();

    /**
     * The ErrorTracker instance used for capturing and anonymizing errors that occur during metrics collection and submission.
     * This error tracker is configured to ignore certain expected exceptions, such as InvocationTargetException with specific
     * messages and AccessDeniedException, which may occur in normal operation and do not indicate issues that need to be tracked.
     * Additionally, the error tracker is set up to anonymize sensitive information such as email addresses, bearer tokens, AWS keys,
     * UUIDs, and API keys or tokens in query parameters. This ensures that any errors captured by the tracker do not contain personally
     * identifiable information or sensitive data, while still allowing for effective tracking and analysis of errors that may occur during
     * the metrics collection process.
     */
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextUnaware()
            .ignoreError(InvocationTargetException.class, "Expected .* but got .*")
            .ignoreError(AccessDeniedException.class)
            .anonymize("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "[email hidden]")
            .anonymize("Bearer [A-Za-z0-9._~+/=-]+", "Bearer [token hidden]")
            .anonymize("AKIA[0-9A-Z]{16}", "[aws-key hidden]")
            .anonymize("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "[uuid hidden]")
            .anonymize("([?&](?:api_?key|token|secret)=)[^&\\s]+", "$1[redacted]");

    /**
     * Gets the UniversalMetricContext instance used for metrics collection and submission. This context is initialized with the mod ID,
     * metrics token, and a custom metric for tracking registered mods. The context also integrates with the error tracker to capture and
     * anonymize errors that occur during metrics collection and submission. Consumers can use this context to submit custom metrics or
     * track errors in their own code, while benefiting from the common configuration and integration provided by this manager.
     * @return the UniversalMetricContext instance used for metrics collection and submission. This context is initialized with the mod ID,
     * metrics token, and a custom metric for tracking registered mods, as well as integration with the error tracker for capturing and
     * anonymizing errors. Consumers can use this context to submit custom metrics or track errors in their own code, while benefiting from
     * the common configuration and integration provided by this manager.
     */
    public static UniversalMetricContext getMetricContext() {
        return metricContext;
    }
}
