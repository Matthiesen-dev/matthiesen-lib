package dev.matthiesen.common.matthiesen_lib.core.interfaces;

/**
 * Relative insertion order for HUD layers.
 */
public enum MatthiesenLibHudOrdering {
    /**
     * Render the new layer before the specified existing layer, or at the bottom of the stack if {@code other} is null.
     */
    BEFORE,
    /**
     * Render the new layer after the specified existing layer, or at the top of the stack if {@code other} is null.
     */
    AFTER
}

