package dev.matthiesen.common.matthiesen_lib_api.core;

import java.util.HashMap;
import java.util.Map;

/**
 * MatthiesenLibReloadManager is responsible for managing reload runnables for mods that integrate with MatthiesenLib.
 * Mods can register a reload runnable that will be executed when the reload command is triggered.
 * This allows mods to perform custom actions during a reload, such as reloading configurations or refreshing data.
 * The reload runnables are stored in a map, where the key is the mod ID and the value is the runnable to execute.
 * Mods should ensure that they register their reload runnable during their initialization phase, and that the runnable is
 * thread-safe and does not perform long-running operations to avoid blocking the main thread during a reload.
 */
public final class MatthiesenLibReloadManager {
    private static final Map<String, Runnable> reloadRunnables = new HashMap<>();
    private static boolean initialized;

    /**
     * Default constructor for the ReloadManager class. No initialization is required as setup is handled in the modInitializer method.
     */
    private MatthiesenLibReloadManager() {}

    /**
     * Initializes the reload manager. This method should be called during the mod initialization phase to set up the manager.
     */
    public static void modInitializer() {
        if (initialized) return;

        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initializing Reload Manager");
    }

    /**
     * Registers a reload runnable for a mod. This runnable will be executed when the reload command is triggered.
     * @param modId The ID of the mod registering the reload runnable. This should be a unique identifier for the mod,
     *              typically the mod ID used in the mod's metadata.
     * @param runnable The runnable to execute during a reload. This should contain the logic that the mod wants to
     *                 perform when a reload is triggered, such as reloading configurations or refreshing data.
     */
    public static void registerReloadRunnable(String modId, Runnable runnable) {
        if (reloadRunnables.containsKey(modId)) {
            MatthiesenLibApiConstants.createErrorLog("A reload runnable is already registered for mod: " + modId);
            return;
        }
        reloadRunnables.put(modId, runnable);
        MatthiesenLibApiConstants.createInfoLog("Registered reload runnable for mod: " + modId);
    }

    /**
     * Retrieves the map of registered reload runnables. This can be used by the reload command to execute all registered runnables during a reload.
     * @return A map where the key is the mod ID and the value is the runnable to execute during a reload.
     */
    public static Map<String, Runnable> getReloadRunnables() {
        return reloadRunnables;
    }
}
