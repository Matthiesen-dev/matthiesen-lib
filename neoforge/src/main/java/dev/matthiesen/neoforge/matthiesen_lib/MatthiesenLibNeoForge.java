package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.neoforge.matthiesen_lib.helper.NeoForgeRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Main class for the MatthiesenLib mod on the NeoForge platform.
 */
@Mod(Constants.MOD_ID)
public class MatthiesenLibNeoForge {

    /**
     * Default constructor for the MatthiesenLibNeoForge class. No initialization is required as setup is handled in the constructor that takes an IEventBus parameter.
     *
     * @param modBus The event bus to register mod events on. This constructor is used for NeoForge's event-driven initialization process.
     */
    public MatthiesenLibNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        MatthiesenLib.modInitializer();
    }
}
