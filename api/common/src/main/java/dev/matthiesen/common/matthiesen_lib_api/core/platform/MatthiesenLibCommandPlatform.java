package dev.matthiesen.common.matthiesen_lib_api.core.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibCommandRegistrar;

import java.util.function.Consumer;

/**
 * Interface for command registration across mod loaders.
 */
@FunctionalInterface
public interface MatthiesenLibCommandPlatform {
    /**
     * Invokes the handler with a platform-specific registrar at command registration time.
     * @param registrationHandler The handler to invoke with the platform-specific CommandRegistrar.
     */
    void registerCommands(Consumer<MatthiesenLibCommandRegistrar> registrationHandler);
}

