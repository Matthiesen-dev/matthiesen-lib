package dev.matthiesen.common.matthiesen_lib.core.interfaces;

import dev.matthiesen.api.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of the API text parser contract that also exposes optional Embers compatibility hooks.
 *
 * <p>This interface remains in the common module so Ember's client-side integration can stay out of
 * the server-safe API package.</p>
 */
public interface MatthiesenLibExtendedTextParser extends MatthiesenLibTextParser {

    /**
     * Gets an optional compatibility implementation for the Embers mod. If the text parser has specific compatibility features for Embers,
     * it can return an instance of MatthiesenLibEmbersTextParserCompat. If not, it should return null.
     * @return An instance of MatthiesenLibEmbersTextParserCompat if the text parser has Embers compatibility features, or null if it does not.
     * This allows for optional integration with the Embers mod without requiring all text parsers to implement Embers-specific functionality.
     */
    @SuppressWarnings("unused")
    default @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        return null;
    }
}

