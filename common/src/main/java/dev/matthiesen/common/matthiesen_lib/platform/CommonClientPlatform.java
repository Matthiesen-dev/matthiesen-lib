package dev.matthiesen.common.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;

import java.util.function.Consumer;

/**
 * Interface for client-specific platform implementations. This allows for registering client-only features such as menu screens without causing issues on server environments.
 */
@SuppressWarnings("unused")
public interface CommonClientPlatform {
    /**
     * Invokes the registration callback with a platform-specific ScreenRegistrar at the correct client lifecycle stage.
     */
    void registerMenuScreens(Consumer<ScreenRegistrar> registrationHandler);
}
