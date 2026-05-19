package dev.matthiesen.common.matthiesen_lib.core.interfaces;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for parsing text into Minecraft Components. This allows for different implementations of text parsing,
 *  such as using MiniMessage or vanilla Minecraft formatting.
 * Implementations of this interface should provide a way to convert a string of text into a Component that can be used in
 * Minecraft chat, tooltips, or other text displays. This is useful for allowing mod developers to use different text parsing
 * libraries without being tied to a specific one, and for providing a consistent way to handle text parsing
 * across different mods and platforms.
 */
public interface MatthiesenLibTextParser {
    /**
     * Initializes the text parser. This method can be used to set up any necessary resources or configurations for the text parser.
     */
    default void initialize() {
        MatthiesenLibConstants.createInfoLog("Initializing text parser: " + getType());
    }

    /**
     * Gets the type of the text parser. This is used to identify the parser and allow for different implementations to be registered and used.
     * @return A string representing the type of the text parser. This should be unique for each implementation to avoid conflicts when multiple parsers are registered.
     */
    String getType();

    /**
     * Parses the given text into a Minecraft Component. The implementation of this method will depend on the specific text parsing library being used.
     * @param text The string of text to parse. This may include formatting codes or other special syntax depending on the implementation of the text parser.
     * @return A Component representing the parsed text. This can be used in Minecraft chat, tooltips, or other text displays. The exact formatting and behavior of the resulting Component will depend on the implementation of the text parser and the input text.
     */
    @SuppressWarnings("unused")
    Component parse(String text);

    /**
     * Gets an optional compatibility implementation for the Embers mod. If the text parser has specific compatibility features for Embers,
     * it can return an instance of MatthiesenLibEmbersTextParserCompat. If not, it should return null.
     * @return An instance of MatthiesenLibEmbersTextParserCompat if the text parser has Embers compatibility features, or null if it does not.
     * This allows for optional integration with the Embers mod without requiring all text parsers to implement Embers-specific functionality.
     */
    @SuppressWarnings("unused")
    @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat();
}
