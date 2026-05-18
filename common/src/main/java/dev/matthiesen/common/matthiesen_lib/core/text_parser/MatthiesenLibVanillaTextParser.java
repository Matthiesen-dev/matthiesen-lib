package dev.matthiesen.common.matthiesen_lib.core.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibTextParser;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the MatthiesenLibTextParser interface that uses vanilla Minecraft formatting codes. This parser
 * will convert '&' characters to '§' characters, allowing for the use of Minecraft's built-in formatting codes in text.
 */
public final class MatthiesenLibVanillaTextParser implements MatthiesenLibTextParser {
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

    @Override
    public @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        MatthiesenLibConstants.createInfoLog("VanillaTextParser does not have an Embers compatibility implementation, returning null");
        return null;
    }
}
