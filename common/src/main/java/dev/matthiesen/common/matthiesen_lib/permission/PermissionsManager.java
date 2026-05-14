package dev.matthiesen.common.matthiesen_lib.permission;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.interfaces.Permission;
import dev.matthiesen.common.matthiesen_lib.interfaces.PermissionRegistrar;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Centralized permission registry for managing permissions across the application.
 * This registry allows permissions to be registered dynamically, either immediately or through a pending queue
 * if the registrar is not yet available. This follows the same pattern as MatthiesenLibCommands and MatthiesenLibClient.
 */
@SuppressWarnings("unused")
public class PermissionsManager {
    private static final List<Permission> PERMISSIONS = new ArrayList<>();
    private static final Map<ResourceLocation, Permission> PERMISSION_MAP = new HashMap<>();
    private static final List<Permission> PENDING_PERMISSIONS = new ArrayList<>();

    private static PermissionRegistrar activeRegistrar;
    private static boolean initialized;

    /**
     * Default constructor for the PermissionsManager class. No initialization is required as setup is handled in the modInitializer method.
     */
    public PermissionsManager() {}

    /**
     * Initializes the permission registry. This should be called during the mod's initialization phase.
     */
    public synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        Constants.createInfoLog("Initialized permission registry");
    }

    /**
     * Registers a permission. If the registrar is not yet available, the permission is queued for later registration.
     * Safe to call at any time.
     *
     * @param permission The Permission to register.
     */
    public synchronized void registerPermission(Permission permission) {
        if (activeRegistrar != null) {
            activeRegistrar.register(permission);
            addPermission(permission);
            return;
        }

        PENDING_PERMISSIONS.add(permission);
    }

    /**
     * Binds the active PermissionRegistrar and registers any pending permissions.
     * Called during permission system initialization.
     *
     * @param registrar The PermissionRegistrar provided by the permission system.
     */
    public synchronized void bindRegistrar(PermissionRegistrar registrar) {
        activeRegistrar = registrar;

        for (Permission permission : PENDING_PERMISSIONS) {
            registrar.register(permission);
            addPermission(permission);
        }

        PENDING_PERMISSIONS.clear();
    }

    /**
     * Internally adds a permission to the registry without triggering the registrar.
     *
     * @param permission The permission to add.
     */
    private void addPermission(Permission permission) {
        PERMISSIONS.add(permission);
        PERMISSION_MAP.put(permission.getIdentifier(), permission);
    }

    /**
     * Retrieves all registered permissions.
     *
     * @return An unmodifiable list of all permissions.
     */
    public List<Permission> all() {
        return new ArrayList<>(PERMISSIONS);
    }

    /**
     * Looks up a permission by its ResourceLocation identifier.
     *
     * @param identifier The ResourceLocation identifier of the permission.
     * @return The Permission, or null if not found.
     */
    public Permission getPermission(ResourceLocation identifier) {
        return PERMISSION_MAP.get(identifier);
    }

    /**
     * Checks if a permission is registered by its ResourceLocation identifier.
     *
     * @param identifier The ResourceLocation identifier to check.
     * @return true if the permission is registered, false otherwise.
     */
    public boolean hasPermission(ResourceLocation identifier) {
        return PERMISSION_MAP.containsKey(identifier);
    }

    /**
     * Gets the count of registered permissions.
     *
     * @return The number of permissions registered.
     */
    public int getPermissionCount() {
        return PERMISSIONS.size();
    }
}
