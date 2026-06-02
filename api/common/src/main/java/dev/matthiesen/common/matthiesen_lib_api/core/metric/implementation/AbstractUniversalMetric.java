package dev.matthiesen.common.matthiesen_lib_api.core.metric.implementation;

import com.google.gson.JsonObject;
import dev.faststats.SimpleMetrics;
import dev.faststats.config.SimpleConfig;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibModContainer;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.Contract;

/**
 * Base implementation for universal metrics, providing common functionality for both client and server metrics.
 * This class handles the initialization of the mod container and provides a method to append universal data to the metrics JSON object.
 * The actual submission logic and environment-specific data collection are implemented in the subclasses.
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractUniversalMetric extends SimpleMetrics {
    /**
     * The mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     */
    protected final MatthiesenLibModContainer modContainer;

    /**
     * Constructs a new UniversalMetricsImpl instance with the given factory and mod container. This constructor is called by the subclasses to initialize the common functionality of the metrics implementation.
     * @param factory the factory used to create this metrics instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param modContainer the mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     * @throws IllegalStateException if there is an issue with initializing the metrics instance, such as invalid configuration or missing dependencies. The actual conditions for throwing this exception depend on the implementation of the superclass and the context initialization.
     */
    @Async.Schedule
    @Contract(mutates = "io")
    AbstractUniversalMetric(final Factory factory, final MatthiesenLibModContainer modContainer) throws IllegalStateException {
        super(factory);
        this.modContainer = modContainer;
    }

    /**
     * This method is called before the metrics submission process starts. It can be overridden by subclasses to perform any necessary checks or setup before starting the submission of metrics data. The return value indicates whether the submission process should proceed (true) or be aborted (false). In this base implementation, it retrieves the configuration from the context and calls the preSubmissionStart method on it, allowing for any custom logic defined in the configuration to determine whether to proceed with submitting metrics.
     * @return true if the submission process should proceed, false if it should be aborted. The actual logic for determining this is defined in the configuration's preSubmissionStart method, which can include checks such as user consent, environment conditions, or other factors that may affect whether metrics should be submitted.
     */
    @Override
    protected boolean preSubmissionStart() {
        return ((SimpleConfig) context.getConfig()).preSubmissionStart();
    }

    /**
     * Appends universal data to the metrics JSON object. This method is called by the subclasses to add common data fields to the metrics before submission. The data added in this method includes the mod version and platform, which are retrieved from the mod container. This allows for consistent inclusion of these fields in both client and server metrics, providing valuable information about the mod's environment and version for analysis.
     * @param metrics the JsonObject representing the metrics data that will be submitted. This object is modified by adding properties for the mod version and platform, which are obtained from the mod container. Subclasses can call this method to ensure that these universal data fields are included in the metrics submission, regardless of whether it's client or server metrics.
     */
    protected void appendUniversalData(final JsonObject metrics) {
        metrics.addProperty("plugin_version", modContainer.getModVersion());
        metrics.addProperty("server_type", modContainer.getPlatform());
    }
}
