package dev.matthiesen.api.matthiesen_lib_api.core.text_parser;

import dev.matthiesen.api.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.api.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import net.minecraft.network.chat.Component;

/**
 * An implementation of the MatthiesenLibTextParser interface that uses vanilla Minecraft formatting codes.
 */
public class MatthiesenLibVanillaTextParser implements MatthiesenLibTextParser {
    /**
     * Creates a new instance of the VanillaTextParser. This constructor does not perform any initialization, as there are no resources to set up for this parser.
     */
    public MatthiesenLibVanillaTextParser() {}

    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.VANILLA.getName();
    }

    @Override
    public Component parse(String text) {
        text = text.replace("&", "§");
        return Component.literal(text);
    }
}

