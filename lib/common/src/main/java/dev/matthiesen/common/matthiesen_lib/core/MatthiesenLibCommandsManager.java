package dev.matthiesen.common.matthiesen_lib.core;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibCommandsManager} instead. */
@Deprecated
@SuppressWarnings("unused")
public final class MatthiesenLibCommandsManager {
    private MatthiesenLibCommandsManager() {}

    public static void modInitializer() {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibCommandsManager.modInitializer();
    }

    public static void registerCommand(dev.matthiesen.api.matthiesen_lib.command.AbstractCommand command) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibCommandsManager.registerCommand(command);
    }
}

