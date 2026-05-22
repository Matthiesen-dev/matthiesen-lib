package dev.matthiesen.neoforge.matthiesen_lib.text_parser;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.api.matthiesen_lib_api.core.MatthiesenLibTextParserManager;
import dev.matthiesen.api.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibExtendedTextParser;
import dev.matthiesen.common.matthiesen_lib.core.compat.MatthiesenLibEmbersCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.tysontheember.emberstextapi.immersivemessages.api.MarkupParser;
import net.tysontheember.emberstextapi.immersivemessages.api.TextSpan;
import net.tysontheember.emberstextapi.util.StyleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An implementation of the MatthiesenLibTextParser interface for the Embers mod on the NeoForge platform.
 */
public class MatthiesenLibEmbersTextParserNeoForge implements MatthiesenLibExtendedTextParser {
    /**
     * Default constructor for the MatthiesenLibEmbersTextParserNeoForge class.
     */
    public MatthiesenLibEmbersTextParserNeoForge() {}

    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.EMBER.getName();
    }

    @Override
    public Component parse(String text) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            List<TextSpan> spans = MarkupParser.parse(text);
            MutableComponent result = Component.empty();
            for (TextSpan span : spans) {
                // applyTextSpanFormatting handles bold/italic/effects but intentionally skips color
                Style style = StyleUtil.applyTextSpanFormatting(Style.EMPTY, span);
                if (span.getColor() != null) {
                    style = style.withColor(span.getColor());
                }
                result.append(Component.literal(span.getContent()).withStyle(style));
            }
            return result;
        } else {
            MatthiesenLibConstants.createErrorLog("Attempted to parse text with the 'ember' parser, but the Embers mod is not loaded, Falling back to 'vanilla' parser");
            return MatthiesenLibTextParserManager.VANILLA_PARSER.parse(text);
        }
    }

    @Override
    public @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        return MatthiesenLibEmbersCompat.getInstance();
    }
}
