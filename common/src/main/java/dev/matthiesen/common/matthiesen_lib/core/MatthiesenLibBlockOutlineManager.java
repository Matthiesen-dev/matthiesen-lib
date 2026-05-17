package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineContext;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineListener;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineRegistrar;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Internal block outline listener manager used by MatthiesenLibClient.
 */
public final class MatthiesenLibBlockOutlineManager {
    private static final List<MatthiesenLibBlockOutlineListener> REGISTERED_LISTENERS = new CopyOnWriteArrayList<>();
    private static boolean initialized;

    private MatthiesenLibBlockOutlineManager() {}

    /**
     * Initializes the block outline manager.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibConstants.createInfoLog("Initialized client-side block outline manager");
    }

    /**
     * Registers multiple block outline listeners.
     *
     * @param registrarConsumer A consumer that receives a helper to register listeners.
     */
    public static void registerBlockOutlineListeners(Consumer<MatthiesenLibBlockOutlineRegistrar> registrarConsumer) {
        registrarConsumer.accept(REGISTERED_LISTENERS::add);
    }

    /**
     * Registers a single block outline listener.
     *
     * @param listener The listener to register.
     */
    public static void registerBlockOutlineListener(MatthiesenLibBlockOutlineListener listener) {
        REGISTERED_LISTENERS.add(listener);
    }

    /**
     * Dispatches a block outline event to all registered listeners.
     *
     * @param context The event context.
     * @return {@code true} to continue with vanilla rendering, {@code false} to cancel it.
     */
    public static boolean fireBlockOutlineEvent(MatthiesenLibBlockOutlineContext context) {
        for (MatthiesenLibBlockOutlineListener listener : REGISTERED_LISTENERS) {
            try {
                if (!listener.onBlockOutline(context)) {
                    return false;
                }
            } catch (Throwable throwable) {
                MatthiesenLibConstants.createErrorLog("Exception while executing block outline listener", throwable);
            }
        }

        return true;
    }
}

