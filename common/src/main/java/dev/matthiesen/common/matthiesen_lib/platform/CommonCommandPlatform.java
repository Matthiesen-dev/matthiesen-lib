package dev.matthiesen.common.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;

import java.util.function.Consumer;

/**
 * Interface for command registration across mod loaders.
 */
@FunctionalInterface
public interface CommonCommandPlatform {
    /**
     * Invokes the handler with a platform-specific registrar at command registration time.
     */
    void registerCommands(Consumer<CommandRegistrar> registrationHandler);
}

