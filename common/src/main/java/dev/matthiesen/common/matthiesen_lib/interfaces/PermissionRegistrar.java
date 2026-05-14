package dev.matthiesen.common.matthiesen_lib.interfaces;

/**
 * Utility interface for registering permissions through a centralized manager.
 */
@FunctionalInterface
public interface PermissionRegistrar {
    /**
     * Registers a permission with the platform's permission management system.
     * @param permission The permission to be registered, containing necessary information such as name, description, and default access level.
     */
    void register(Permission permission);
}

