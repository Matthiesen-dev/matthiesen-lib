package dev.matthiesen.common.matthiesen_lib.core.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibExtendedTextParser;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the MatthiesenLibTextParser interface that uses vanilla Minecraft formatting codes.
 */
@Deprecated(forRemoval = true)
public class MatthiesenLibVanillaTextParser extends dev.matthiesen.common.matthiesen_lib_api.core.text_parser.MatthiesenLibVanillaTextParser implements MatthiesenLibExtendedTextParser {
    /**
     * Creates a new instance of the VanillaTextParser. This constructor does not perform any initialization, as there are no resources to set up for this parser.
     */
    public MatthiesenLibVanillaTextParser() {}


    @Override
    public @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        MatthiesenLibConstants.createInfoLog("VanillaTextParser does not have an Embers compatibility implementation, returning null");
        return null;
    }
}
