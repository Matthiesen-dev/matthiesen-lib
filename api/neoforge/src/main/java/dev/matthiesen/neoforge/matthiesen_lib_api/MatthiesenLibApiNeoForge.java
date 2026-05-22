package dev.matthiesen.neoforge.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MatthiesenLibApiConstants.MOD_ID)
public class MatthiesenLibApiNeoForge {
    public MatthiesenLibApiNeoForge(IEventBus modBus) {
        MatthiesenLibApiConstants.createInfoLog("Loading API for NeoForge Mod Loader");
    }
}
