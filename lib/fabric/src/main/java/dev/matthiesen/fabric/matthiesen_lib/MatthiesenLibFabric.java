package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.fabric.matthiesen_lib.text_parser.MatthiesenLibEmbersTextParserFabric;
import net.fabricmc.api.ModInitializer;

/**
 * Main class for the MatthiesenLib mod on the Fabric platform.
 */
public class MatthiesenLibFabric implements ModInitializer {
    /**
     * Default constructor for the MatthiesenLibFabric class. No initialization is required as setup is handled in the onInitialize method.
     */
    public MatthiesenLibFabric() {}

    /**
     * Initializes the MatthiesenLib mod for the Fabric platform.
     */
    @Override
    public void onInitialize() {
        MatthiesenLibConstants.createInfoLog("Loading for Fabric Mod Loader");
        MatthiesenLib.modInitializer();
        if (MatthiesenLibApi.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            MatthiesenLib.registerTextParser(new MatthiesenLibEmbersTextParserFabric());
        }
    }
}
