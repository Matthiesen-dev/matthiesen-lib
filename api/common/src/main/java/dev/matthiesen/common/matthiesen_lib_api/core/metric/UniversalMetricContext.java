package dev.matthiesen.common.matthiesen_lib_api.core.metric;

import dev.faststats.Metrics;
import dev.faststats.SimpleContext;
import dev.faststats.SimpleMetrics;
import dev.faststats.Token;
import dev.faststats.config.SimpleConfig;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibModContainer;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

/**
 * UniversalMetricContext is a specialized context for collecting and submitting metrics data for a specific mod. It extends the SimpleContext from the FastStats library and provides mod-specific information such as the mod version and platform. This context is designed to be used with the UniversalMetrics implementation, which handles the actual collection and submission of metrics data. The UniversalMetricContext initializes the mod container based on the provided mod ID and ensures that the necessary services for metrics collection are set up correctly. It also overrides the getProjectName method to return a unique identifier for the mod, which is used in the metrics submission process to associate the collected data with the correct mod.
 * The Factory inner class provides a convenient way to create instances of UniversalMetricContext by accepting the mod ID and token as parameters. This allows for easy integration of metrics collection into mods by simply creating a new Factory instance with the appropriate mod ID and token, and then calling the create method to obtain a UniversalMetricContext instance ready for use with the UniversalMetrics implementation.
 */
@SuppressWarnings("UnstableApiUsage")
public class UniversalMetricContext extends SimpleContext {
    final MatthiesenLibModContainer mod;

    /**
     * Constructs a new UniversalMetricContext instance with the given factory, mod ID, and token. This constructor initializes the mod container based on the provided mod ID and sets up the necessary services for metrics collection. It also ensures that the mod with the specified ID exists, throwing an IllegalArgumentException if it does not. The context is initialized with a configuration read from the mod's config file, allowing for customizable behavior based on the mod's settings. This context is designed to be used with the UniversalMetrics implementation, which will utilize this context to collect and submit metrics data specific to the mod.
     * @param factory the factory used to create this context instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param modId the unique identifier of the mod for which this metrics context is being created. This is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data.
     * @param token the token used for authentication or identification purposes in the metrics submission process. This is passed to the superclass constructor to initialize the context and may be used in the metrics submission process to authenticate or identify the source of the metrics data.
     */
    private UniversalMetricContext(final Factory factory, final String modId, @Token final String token) {
        super(factory, getPlatformConfig(), "matthiesen_lib_universal", token);
        this.mod = MatthiesenLibApi.getModContainer(modId);
        if (mod == null) throw new IllegalArgumentException("Mod with id '" + modId + "' not found");
        initializeServices(factory);
    }

    public static SimpleConfig getPlatformConfig() {
        return SimpleConfig.read(MatthiesenLibApi.getModConfig(MatthiesenLibApiConstants.MOD_ID, "metrics.properties"));
    }

    /**
     * Returns the project name for this metrics context, which is the unique identifier of the mod associated with this context. This method is used in the metrics submission process to associate the collected data with the correct mod. The project name is obtained from the mod container's getModMetricId method, ensuring that it is unique and consistent with the mod's information. This allows for accurate tracking and analysis of metrics data for each individual mod using this UniversalMetricContext.
     * @return the project name for this metrics context, which is the unique identifier of the mod associated with this context. This value is used in the metrics submission process to associate the collected data with the correct mod, allowing for accurate tracking and analysis of metrics data for each individual mod using this UniversalMetricContext.
     */
    @Override
    public @NonNull String getProjectName() {
        return mod.getModMetricId();
    }

    /**
     * Creates and returns a Metrics.Factory instance for this context. This factory is responsible for creating Metrics instances that will be used to collect and submit metrics data for the mod associated with this context. The factory implementation checks the environment type (client or server) and creates the appropriate Metrics implementation (UniversalMetricsClientImpl for client and UniversalMetricsServerImpl for server). This allows for environment-specific handling of metrics collection and submission while still utilizing the common functionality provided by the UniversalMetricsImpl base class.
     * The Metrics instances created by this factory will utilize the mod information from the context to include relevant data in the metrics submissions, ensuring that the collected data is associated with the correct mod and environment. This design allows for flexible and efficient metrics collection tailored to the specific needs of both client and server environments while maintaining a consistent interface for mods to use when integrating metrics collection into their functionality.
     * @return a Metrics.Factory instance for this context, which creates Metrics instances that will be used to collect and submit metrics data for the mod associated with this context. The factory implementation checks the environment type (client or server) and creates the appropriate Metrics implementation (UniversalMetricsClientImpl for client and UniversalMetricsServerImpl for server), allowing for environment-specific handling of metrics collection and submission while still utilizing the common functionality provided by the UniversalMetricsImpl base class.
     */
    @Override
    @Contract(value = " -> new", pure = true)
    protected Metrics.@NonNull Factory metricsFactory() {
        return new SimpleMetrics.Factory(this) {
            @Override
            public @NonNull Metrics create() throws IllegalStateException {
                final var mod = ((UniversalMetricContext) context).mod;
                return switch (MatthiesenLibApi.getEnvironmentType()) {
                    case CLIENT -> new UniversalMetricsClientImpl(this, mod);
                    case SERVER -> new UniversalMetricsServerImpl(this, mod);
                };
            }
        };
    }

    /**
     * Factory class for creating instances of UniversalMetricContext. This factory accepts the mod ID and token as parameters and provides a create method to instantiate a new UniversalMetricContext with the specified mod information. By using this factory, mods can easily integrate metrics collection by simply providing their mod ID and token, allowing for streamlined creation of the necessary context for metrics collection and submission.
     * The Factory class extends the SimpleContext.Factory, providing the necessary functionality to create instances of UniversalMetricContext while also allowing for customization of the context initialization process if needed. This design promotes ease of use and flexibility for mod developers when integrating metrics collection into their mods using the UniversalMetricContext and UniversalMetrics implementations.
     */
    public static final class Factory extends SimpleContext.Factory<UniversalMetricContext, Factory> {
        private final String modId;
        private final @Token String token;

        /**
         * Constructs a new Factory instance for creating UniversalMetricContext instances. This constructor accepts the mod ID and token as parameters, which are used to initialize the necessary information for creating UniversalMetricContext instances. The mod ID is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data, while the token is used for authentication or identification purposes in the metrics submission process. By providing these parameters in the constructor, this Factory allows for streamlined creation of UniversalMetricContext instances with the necessary mod information for metrics collection and submission.
         * @param modId the unique identifier of the mod for which this metrics context will be created. This is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data.
         * @param token the token used for authentication or identification purposes in the metrics submission process. This is passed to the superclass constructor to initialize the context and may be used in the metrics submission process to authenticate or identify the source of the metrics data.
         */
        public Factory(final String modId, @Token final String token) {
            this.modId = modId;
            this.token = token;
        }

        /**
         * Creates and returns a new UniversalMetricContext instance using the provided mod ID and token. This method utilizes the Factory's constructor parameters to initialize the necessary information for creating the UniversalMetricContext, including retrieving the mod container based on the mod ID and setting up the context with the appropriate configuration and services for metrics collection. By calling this create method, mods can easily obtain a UniversalMetricContext instance that is ready for use with the UniversalMetrics implementation, allowing for efficient integration of metrics collection into their functionality.
         * @return a new UniversalMetricContext instance initialized with the mod information and token provided to this Factory. This context is ready for use with the UniversalMetrics implementation, allowing for efficient integration of metrics collection into the mod's functionality. The created UniversalMetricContext will include the necessary mod information and services for collecting and submitting metrics data specific to the mod associated with this context.
         */
        public @NonNull UniversalMetricContext create() {
            return new UniversalMetricContext(this, modId, token);
        }
    }
}
