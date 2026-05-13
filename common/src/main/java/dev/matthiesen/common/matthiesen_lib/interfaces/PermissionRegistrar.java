package dev.matthiesen.common.matthiesen_lib.interfaces;

/**
 * Utility interface for registering permissions through a centralized manager.
 */
@FunctionalInterface
public interface PermissionRegistrar {
    void register(Permission permission);
}

