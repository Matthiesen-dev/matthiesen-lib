package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.matthiesen_lib_api.core.text_parser.MatthiesenLibVanillaTextParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for text parsers in the MatthiesenLib. This class allows for registering and retrieving text parsers by their type,
 * as well as checking if a parser is registered for a given type. It also includes a built-in vanilla text parser that is registered
 * by default and used as a fallback when no parser is found for a requested type.
 */
public final class MatthiesenLibTextParserManager {
    private static final Map<String, MatthiesenLibTextParser> REGISTERED_PARSERS = new ConcurrentHashMap<>();
    private static boolean initialized;

    /**
     * The built-in vanilla text parser. This parser is registered by default during initialization and is used as a fallback when no parser is found for a requested type.
     */
    public static final MatthiesenLibTextParser VANILLA_PARSER = new MatthiesenLibVanillaTextParser();

    /**
     * Initializes the text parser manager. This method should be called during mod initialization to set up the manager and log its initialization.
     */
    private MatthiesenLibTextParserManager() {}

    /**
     * Initializes the screen manager. This method is idempotent and will only perform initialization once, even if called multiple times.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initialized text parser manager");
        registerTextParser(VANILLA_PARSER);
    }

    /**
     * Registers a text parser. This method is thread-safe and can be called at any time. If a parser with the same type is already
     * registered, it will be overwritten.
     * @param parser The text parser to register. The parser's type is determined by its getType() method, and it will be initialized
     *               before being added to the registry.
     */
    public static void registerTextParser(MatthiesenLibTextParser parser) {
        if (isTextParserInitialized(parser.getType())) return;
        parser.initialize();
        REGISTERED_PARSERS.put(parser.getType(), parser);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the
     * vanilla parser is returned as a fallback.
     * @param type The type of the text parser to retrieve. This should match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(String type) {
        var parser = REGISTERED_PARSERS.get(type);
        if (parser == null) {
            MatthiesenLibApiConstants.createErrorLog("Attempted to retrieve text parser of type '" + type + "', but no such parser is registered, Falling back to 'vanilla' parser");
            return VANILLA_PARSER;
        }
        return parser;
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla
     * parser is returned as a fallback.
     * @param type The type of the text parser to retrieve, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(MatthiesenLibBuiltInTextParsers type) {
        return getTextParser(type.getName());
    }

    /**
     * Checks if a text parser is registered for the given type.
     * @param type The type of the text parser to check for. This should match the value returned by the getType() method of the desired parser.
     * @return {@code true} if a text parser is registered for the given type, {@code false} otherwise.
     */
    public static boolean isTextParserInitialized(String type) {
        return REGISTERED_PARSERS.containsKey(type);
    }

    /**
     * Checks if a text parser is registered for the given type.
     * @param type The type of the text parser to check for, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return {@code true} if a text parser is registered for the given type, {@code false} otherwise.
     */
    public static boolean isTextParserInitialized(MatthiesenLibBuiltInTextParsers type) {
        return REGISTERED_PARSERS.containsKey(type.getName());
    }
}

