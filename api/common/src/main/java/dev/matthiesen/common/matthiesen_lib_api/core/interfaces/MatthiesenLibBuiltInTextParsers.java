package dev.matthiesen.common.matthiesen_lib_api.core.interfaces;

/**
 * Enum representing the built-in text parsers available in MatthiesenLib. Each enum constant corresponds to a specific text
 * parser implementation that can be used for parsing and formatting text in Minecraft.
 * The enum provides a way to identify and select the desired text parser when registering or using text parsing functionality in mods that
 * utilize MatthiesenLib. The 'name' field for each enum constant can be used to retrieve the corresponding text parser implementation when needed.
 */
public enum MatthiesenLibBuiltInTextParsers {
    /**
     * The vanilla text parser, which uses Minecraft's built-in formatting codes.
     */
    VANILLA("vanilla"),
    /**
     * Ember's Text API parser, which allows for more advanced text formatting and features beyond what vanilla Minecraft provides.
     * See The <a href="https://tysontheember.dev/embers-text-api/intro/">Ember's Text API Documentation</a> for more information about this text parser
     * and its capabilities.
     */
    EMBER("emberstextapi");

    private final String name;

    /**
     * Constructs a new enum constant for a built-in text parser with the given name. The name is used to identify the text parser and can be used to look
     * up the corresponding implementation when registering and retrieving parsers.
     * @param name The name of the text parser, which should be unique for each built-in parser to avoid conflicts when registering and retrieving parsers.
     *             This name is used as the type identifier for the text parser and can be used to look up the corresponding implementation when needed.
     */
    MatthiesenLibBuiltInTextParsers(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the text parser. This name is used as the type identifier for the text parser and can be used to look up the corresponding
     * implementation when needed.
     * @return The name of the text parser, which should be unique for each built-in parser to avoid conflicts when registering and retrieving parsers.
     * This name is used as the type identifier for the text parser and can be used to look up the corresponding implementation when needed.
     */
    public String getName() {
        return name;
    }
}

