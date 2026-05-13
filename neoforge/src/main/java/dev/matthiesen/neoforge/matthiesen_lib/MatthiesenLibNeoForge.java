package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.neoforge.matthiesen_lib.platform.NeoForgeRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Main class for the MatthiesenLib mod on the NeoForge platform.
 */
@Mod(Constants.MOD_ID)
public class MatthiesenLibNeoForge {
    public MatthiesenLibNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        MatthiesenLib.modInitializer();
    }
}
