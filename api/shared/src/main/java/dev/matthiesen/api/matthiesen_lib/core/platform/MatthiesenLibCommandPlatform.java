package dev.matthiesen.api.matthiesen_lib.core.platform;

import dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibCommandRegistrar;

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

