package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;

/**
 * Abstract base class for command registries. Provides a default implementation of the init method and a static register method to register commands with the MatthiesenLib command system.
 * Subclasses can extend this class to create specific command registries for different modules or features.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommandRegistry {
    /**
     * Default constructor for the AbstractCommandRegistry class. No initialization is required as setup is handled in the init method.
     */
    public AbstractCommandRegistry() {}

    /**
     * Initializes the command registry. This method can be used to perform any necessary setup before registration. You should call this from your Mod Entrypoint.
     */
    public static void init() {}

    /**
     * Registers a command with the MatthiesenLib command system. This method can be called at any time, and the command will be registered appropriately based on the state of the command registrar.
     * @param command The AbstractCommand to register. This command will be added to the MatthiesenLib command system and will be available for use once the registrar is ready.
     */
    public static void register(AbstractCommand command) {
        MatthiesenLib.registerCommand(command);
    }
}
