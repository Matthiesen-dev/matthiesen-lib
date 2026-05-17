package dev.matthiesen.common.matthiesen_lib.core.interfaces;

/**
 * Listener invoked when a block outline highlight is about to be rendered.
 */
@FunctionalInterface
public interface MatthiesenLibBlockOutlineListener {
    /**
     * Called before the default block outline is rendered.
     *
     * @param context The current block outline render context.
     * @return {@code true} to continue with default rendering, {@code false} to cancel it.
     */
    boolean onBlockOutline(MatthiesenLibBlockOutlineContext context);
}

