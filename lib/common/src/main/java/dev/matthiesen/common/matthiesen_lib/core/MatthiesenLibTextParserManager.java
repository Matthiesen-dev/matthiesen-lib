package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibExtendedTextParser;
import dev.matthiesen.common.matthiesen_lib.core.text_parser.MatthiesenLibVanillaTextParser;
import net.minecraft.network.chat.Component;

/** @deprecated Use {@link dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager} instead. */
@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public final class MatthiesenLibTextParserManager {
    /**
     * Common compatibility vanilla parser that preserves the Embers compatibility hook on the legacy interface.
     */
    public static final MatthiesenLibExtendedTextParser VANILLA_PARSER = new MatthiesenLibVanillaTextParser();

    private MatthiesenLibTextParserManager() {}

    public static synchronized void modInitializer() {
        dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager.modInitializer();
    }

    public static void registerTextParser(MatthiesenLibExtendedTextParser parser) {
        dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager.registerTextParser(parser);
    }

    public static MatthiesenLibExtendedTextParser getTextParser(String type) {
        return adapt(dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager.getTextParser(type));
    }

    public static MatthiesenLibExtendedTextParser getTextParser(MatthiesenLibBuiltInTextParsers type) {
        return getTextParser(type.getName());
    }

    public static boolean isTextParserInitialized(String type) {
        return dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager.isTextParserInitialized(type);
    }

    public static boolean isTextParserInitialized(MatthiesenLibBuiltInTextParsers type) {
        return isTextParserInitialized(type.getName());
    }

    private static MatthiesenLibExtendedTextParser adapt(MatthiesenLibTextParser parser) {
        if (parser instanceof MatthiesenLibExtendedTextParser commonParser) {
            return commonParser;
        }

        if (dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers.VANILLA.getName().equals(parser.getType())) {
            return VANILLA_PARSER;
        }

        return new LegacyTextParserAdapter(parser);
    }

    private record LegacyTextParserAdapter(MatthiesenLibTextParser delegate)
            implements MatthiesenLibExtendedTextParser {
        @Override
        public void initialize() {
            delegate.initialize();
        }

        @Override
        public String getType() {
            return delegate.getType();
        }

        @Override
        public Component parse(String text) {
            return delegate.parse(text);
        }
    }
}
