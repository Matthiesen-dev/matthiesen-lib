package dev.matthiesen.common.matthiesen_lib.core.interfaces;

/**
 * Registrar contract for queueing keybind registrations.
 */
@FunctionalInterface
public interface MatthiesenLibKeybindRegistrar {
    void registerKeybind(String name, MatthiesenLibKeybindMapping keybind);
}

