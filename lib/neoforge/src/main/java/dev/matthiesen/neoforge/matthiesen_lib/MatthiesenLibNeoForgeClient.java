package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

/**
 * Client-side initialization class for MatthiesenLib on the NeoForge platform, responsible for setting up client-specific resources and event listeners.
 */
@Mod(value = MatthiesenLibConstants.MOD_ID, dist = Dist.CLIENT)
public class MatthiesenLibNeoForgeClient {

    /**
     * Default constructor for the MatthiesenLibNeoForgeClient class. Initializes client-side resources and registers event listeners for screen registration.
     */
    public MatthiesenLibNeoForgeClient() {
        MatthiesenLibConstants.createInfoLog("Loading Client resources for NeoForge Mod Loader");
        MatthiesenLibClient.modInitializer();
    }
}
