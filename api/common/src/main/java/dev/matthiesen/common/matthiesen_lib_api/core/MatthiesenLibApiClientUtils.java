package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Utility class for client-specific operations, such as registering runnables to be executed during client initialization.
 * This allows for deferred execution of client-only code without directly referencing client-side classes in common code.
 * The ClientUtils class uses a service loader to find the appropriate platform implementation of MatthiesenLibClientPlatform, which handles the actual registration of client load runnables.
 * This design ensures that client-specific code can be executed at the correct time during the client's lifecycle without causing class loading issues on the server side.
 */
public final class MatthiesenLibApiClientUtils {
    private static final MatthiesenLibClientPlatform PLATFORM =
            ServiceLoader.load(MatthiesenLibClientPlatform.class).findFirst().orElseThrow();
    private static final List<Runnable> CLIENT_LOAD_RUNNABLES = new java.util.ArrayList<>();

    /**
     * Private constructor to prevent instantiation of this utility class. All methods are static and should be accessed directly through the class name.
     */
    private MatthiesenLibApiClientUtils() {}

    /**
     * Appends a Runnable to the list of client load runnables. These runnables will be executed during the client's initialization phase.
     * @param runnable The Runnable to be executed on client load. This can contain any client-specific code that needs to run during initialization.
     */
    public static void appendRunnableShutdown(Runnable runnable) {
        synchronized (CLIENT_LOAD_RUNNABLES) {
            CLIENT_LOAD_RUNNABLES.add(runnable);
        }
    }

    /**
     * Registers the client load runnables with the platform-specific client load event. This should be called during the mod's initialization phase to ensure that all appended runnables are executed when the client loads.
     */
    public static void registerClientLoadRunnables() {
        PLATFORM.onClientShutdown(CLIENT_LOAD_RUNNABLES);
    }
}
