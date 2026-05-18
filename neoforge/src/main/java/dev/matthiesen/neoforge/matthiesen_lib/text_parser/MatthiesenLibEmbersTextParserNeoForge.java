package dev.matthiesen.neoforge.matthiesen_lib.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.matthiesen_lib.core.text_parser.MatthiesenLibEmbersCompat;
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
public class MatthiesenLibEmbersTextParserNeoForge implements MatthiesenLibTextParser {
    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.EMBER.getName();
    }

    @Override
    public Component parse(String text) {
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
    }

    @Override
    public @Nullable MatthiesenLibEmbersTextParserCompat getEmbersCompat() {
        return MatthiesenLibEmbersCompat.getInstance();
    }
}
