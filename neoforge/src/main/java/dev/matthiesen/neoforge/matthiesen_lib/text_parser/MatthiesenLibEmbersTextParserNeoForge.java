package dev.matthiesen.neoforge.matthiesen_lib.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibTextParser;
import net.minecraft.network.chat.Component;

/**
 * An implementation of the MatthiesenLibTextParser interface for the Embers mod on the NeoForge platform.
 */
public class MatthiesenLibEmbersTextParserNeoForge implements MatthiesenLibTextParser {
    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.EMBER.getName();
    }

    @Override
    public Component parse(String text) {
        return null;
    }
}
