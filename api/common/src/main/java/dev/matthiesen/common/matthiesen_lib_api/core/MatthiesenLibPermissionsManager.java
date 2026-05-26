package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized permission registry for managing permissions across the application.
 * This registry allows permissions to be registered dynamically, either immediately or through a pending queue
 * if the registrar is not yet available. This follows the same pattern as MatthiesenLibCommands and MatthiesenLibClient.
 */
public final class MatthiesenLibPermissionsManager {
    private static final List<Permission> PERMISSIONS = new ArrayList<>();
    private static final List<Permission> PENDING_PERMISSIONS = new ArrayList<>();
    private static boolean initialized;

    /**
     * Default constructor for the PermissionsManager class. No initialization is required as setup is handled in the modInitializer method.
     */
    private MatthiesenLibPermissionsManager() {}

    /**
     * Initializes the permission registry. This should be called during the mod's initialization phase.
     */
    public static synchronized void modInitializer() {
        if (initialized) return;
        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initialized permission registry");
    }

    /**
     * Registers a permission. If the registrar is not yet available, the permission is queued for later registration.
     * Safe to call at any time.
     *
     * @param permission The Permission to register.
     */
    public static synchronized void registerPermission(Permission permission) {
        PENDING_PERMISSIONS.add(permission);
    }

    /**
     * Internally adds a permission to the registry without triggering the registrar.
     *
     * @param permission The permission to add.
     */
    private static void addPermission(Permission permission) {
        PERMISSIONS.add(permission);
    }

    /**
     * Retrieves all registered permissions.
     *
     * @return An unmodifiable list of all permissions.
     */
    public static List<Permission> all() {
        for (Permission permission : PENDING_PERMISSIONS) addPermission(permission);
        PENDING_PERMISSIONS.clear();
        return new ArrayList<>(PERMISSIONS);
    }

    /**
     * Gets the count of registered pending permissions.
     *
     * @return The number of permissions pending registration.
     */
    public static int getPendingPermissionCount() {
        return PENDING_PERMISSIONS.size();
    }
}

