package dev.matthiesen.common.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;

/**
 * Interface for client-specific platform implementations. This allows for registering client-only features such as menu screens without causing issues on server environments.
 */
@SuppressWarnings("unused")
public interface CommonClientPlatform {
    /**
     * Registers menu screens using the provided ScreenRegistrar. This method should be called during client initialization to ensure that all menu screens are properly registered.
     */
    void registerMenuScreens(ScreenRegistrar registrar);
}
