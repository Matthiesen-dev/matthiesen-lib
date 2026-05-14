package dev.matthiesen.common.matthiesen_lib.interfaces;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;

/**
 * Utility interface for registering Brigadier commands through platform callbacks.
 */
@FunctionalInterface
public interface CommandRegistrar {

    /**
     * Registers a command with the platform's command dispatcher. The provided AbstractCommand will be registered according
     * to the platform's specific registration process.
     * @param command The AbstractCommand instance to register. This command will be registered with the platform's command
     *                dispatcher when this method is called. The command's register method will be invoked with the
     *                appropriate parameters for the platform.
     */
    void register(AbstractCommand command);
}

