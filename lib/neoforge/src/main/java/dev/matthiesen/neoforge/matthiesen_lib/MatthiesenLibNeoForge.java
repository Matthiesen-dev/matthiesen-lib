package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCreativeModeTabSectionsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.neoforge.matthiesen_lib.text_parser.MatthiesenLibEmbersTextParserNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Main class for the MatthiesenLib mod on the NeoForge platform.
 */
@Mod(MatthiesenLibConstants.MOD_ID)
public class MatthiesenLibNeoForge {
    /**
     * Default constructor for the MatthiesenLibNeoForge class. No initialization is required as setup is handled in the constructor that
     * takes an IEventBus parameter.
     *
     * @param modBus The event bus to register mod events on. This constructor is used for NeoForge's event-driven initialization process.
     */
    public MatthiesenLibNeoForge(IEventBus modBus) {
        MatthiesenLibConstants.createInfoLog("Loading for NeoForge Mod Loader");
        MatthiesenLib.modInitializer();
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            MatthiesenLib.registerTextParser(new MatthiesenLibEmbersTextParserNeoForge());
        }
        modBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MatthiesenLibCreativeModeTabSectionsManager::runAutoRegistrations);
    }
}
