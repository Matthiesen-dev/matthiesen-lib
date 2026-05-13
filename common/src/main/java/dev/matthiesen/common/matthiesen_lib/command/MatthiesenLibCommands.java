package dev.matthiesen.common.matthiesen_lib.command;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonCommandPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Unified command registry for all supported loaders.
 */
@SuppressWarnings("unused")
public final class MatthiesenLibCommands {
    private static final CommonCommandPlatform COMMON_COMMAND_PLATFORM =
            ServiceLoader.load(CommonCommandPlatform.class).findFirst().orElseThrow();

    private static final List<AbstractCommand> PENDING_COMMANDS = new ArrayList<>();

    private static CommandRegistrar activeRegistrar;
    private static boolean initialized;

    private MatthiesenLibCommands() {
    }

    /**
     * Initializes command hooks for the active platform.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        COMMON_COMMAND_PLATFORM.registerCommands(MatthiesenLibCommands::bindRegistrar);
        Constants.createInfoLog("Initialized command registry");
    }

    /**
     * Registers an AbstractCommand using the platform-specific command callback.
     */
    public static synchronized void registerCommand(AbstractCommand command) {
        if (activeRegistrar != null) {
            activeRegistrar.register(command);
            return;
        }

        PENDING_COMMANDS.add(command);
    }

    private static synchronized void bindRegistrar(CommandRegistrar registrar) {
        activeRegistrar = registrar;

        for (AbstractCommand command : PENDING_COMMANDS) {
            registrar.register(command);
        }

        PENDING_COMMANDS.clear();
    }
}

