package dev.matthiesen.common.matthiesen_lib.interfaces;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;

/**
 * Utility interface for registering Brigadier commands through platform callbacks.
 */
@FunctionalInterface
public interface CommandRegistrar {
    void register(AbstractCommand command);
}

