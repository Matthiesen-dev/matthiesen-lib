package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.api.matthiesen_lib_api.command.AbstractCommand;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib_api.core.MatthiesenLibCommandsManager} instead. */
@Deprecated
@SuppressWarnings("unused")
public final class MatthiesenLibCommandsManager {
    private MatthiesenLibCommandsManager() {}

    public static void modInitializer() {
        dev.matthiesen.api.matthiesen_lib_api.core.MatthiesenLibCommandsManager.modInitializer();
    }

    public static void registerCommand(AbstractCommand command) {
        dev.matthiesen.api.matthiesen_lib_api.core.MatthiesenLibCommandsManager.registerCommand(command);
    }
}

