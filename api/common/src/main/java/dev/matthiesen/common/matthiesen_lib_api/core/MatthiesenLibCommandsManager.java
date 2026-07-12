package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibCommandRegistrar;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibCommandPlatform;

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Unified command registry for all supported loaders.
 * <p>
 * Commands are kept in a permanent store ({@code REGISTERED_COMMANDS}) so that every entry can be
 * replayed against a fresh {@link MatthiesenLibCommandRegistrar} on each server reload.
 */
public final class MatthiesenLibCommandsManager {
    private static final MatthiesenLibCommandPlatform COMMON_COMMAND_PLATFORM =
            ServiceLoader.load(MatthiesenLibCommandPlatform.class).findFirst().orElseThrow();

    /**
     * Permanent store of every command that has ever been registered.
     * Never cleared — replayed in full on every {@link #bindRegistrar} call (i.e. every server reload).
     */
    private static final List<AbstractCommand> REGISTERED_COMMANDS = new CopyOnWriteArrayList<>();

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
        MatthiesenLibApiConstants.createExtendedLog("Initialized command registry");
    }

    /**
     * Registers an {@link AbstractCommand} with the platform-specific command callback.
     * <p>
     * The command is always added to the permanent {@code REGISTERED_COMMANDS} store so it will be
     * re-registered automatically on every subsequent server reload. If the command registrar is
     * already active (i.e. the initial registration event has already fired) the command is also
     * registered immediately with the current registrar.
     *
     * @param command The {@link AbstractCommand} instance to register.
     */
    public static synchronized void registerCommand(AbstractCommand command) {
        REGISTERED_COMMANDS.add(command);

        if (activeRegistrar != null) {
            activeRegistrar.register(command);
        }
    }

    /**
     * Returns an unmodifiable view of all commands that have been registered so far.
     *
     * @return An unmodifiable {@link List} of every registered {@link AbstractCommand}.
     */
    @SuppressWarnings("unused")
    public static List<AbstractCommand> getAllRegisteredCommands() {
        return Collections.unmodifiableList(REGISTERED_COMMANDS);
    }

    /**
     * Binds the active {@link MatthiesenLibCommandRegistrar} and replays the full
     * {@code REGISTERED_COMMANDS} store against it. Called by each platform on every command
     * registration event — including server reloads — ensuring all commands remain registered
     * across the server lifecycle.
     *
     * @param registrar The {@link MatthiesenLibCommandRegistrar} provided by the platform's command registration event.
     */
    private static synchronized void bindRegistrar(MatthiesenLibCommandRegistrar registrar) {
        activeRegistrar = registrar;

        for (AbstractCommand command : REGISTERED_COMMANDS) {
            registrar.register(command);
        }
    }
}

