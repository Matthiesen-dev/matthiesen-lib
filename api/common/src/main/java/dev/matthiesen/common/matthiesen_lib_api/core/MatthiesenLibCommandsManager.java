package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibCommandRegistrar;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibCommandPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Unified command registry for all supported loaders.
 */
public final class MatthiesenLibCommandsManager {
    private static final MatthiesenLibCommandPlatform COMMON_COMMAND_PLATFORM =
            ServiceLoader.load(MatthiesenLibCommandPlatform.class).findFirst().orElseThrow();

    private static final List<AbstractCommand> PENDING_COMMANDS = new ArrayList<>();

    private static MatthiesenLibCommandRegistrar activeRegistrar;
    private static boolean initialized;

    private MatthiesenLibCommandsManager() {}

    /**
     * Initializes command hooks for the active platform.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        COMMON_COMMAND_PLATFORM.registerCommands(MatthiesenLibCommandsManager::bindRegistrar);
        MatthiesenLibApiConstants.createInfoLog("Initialized command registry");
    }

    /**
     * Registers an AbstractCommand using the platform-specific command callback.
     * If the callback has not yet been fired, the command is added to a pending list to be registered when the callback is received.
     *
     * @param command The AbstractCommand instance to register.
     */
    public static synchronized void registerCommand(AbstractCommand command) {
        if (activeRegistrar != null) {
            activeRegistrar.register(command);
            return;
        }

        PENDING_COMMANDS.add(command);
    }

    /**
     * Binds the active CommandRegistrar to the static field and registers any pending commands. Called by each platform during its command registration event.
     *
     * @param registrar The CommandRegistrar provided by the platform's command registration event.
     */
    private static synchronized void bindRegistrar(MatthiesenLibCommandRegistrar registrar) {
        activeRegistrar = registrar;

        for (AbstractCommand command : PENDING_COMMANDS) {
            registrar.register(command);
        }

        PENDING_COMMANDS.clear();
    }
}

