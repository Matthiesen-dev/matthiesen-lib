package dev.matthiesen.common.matthiesen_lib.core.interfaces;

/**
 * Utility interface for registering block outline listeners.
 */
@FunctionalInterface
public interface MatthiesenLibBlockOutlineRegistrar {
    /**
     * Registers a block outline listener.
     *
     * @param listener The listener to register.
     */
    void register(MatthiesenLibBlockOutlineListener listener);
}

