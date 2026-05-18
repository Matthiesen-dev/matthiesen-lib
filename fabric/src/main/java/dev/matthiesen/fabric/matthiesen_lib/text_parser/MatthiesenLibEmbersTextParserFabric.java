package dev.matthiesen.fabric.matthiesen_lib.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.matthiesen_lib.core.text_parser.MatthiesenLibEmbersCompat;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the MatthiesenLibTextParser interface for the Embers mod on the NeoForge platform.
 */
public class MatthiesenLibEmbersTextParserFabric implements MatthiesenLibTextParser {
    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.EMBER.getName();
    }

    @Override
    public Component parse(String text) {
        return null;
    }

    @Override
    public @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        return MatthiesenLibEmbersCompat.getInstance();
    }
}
