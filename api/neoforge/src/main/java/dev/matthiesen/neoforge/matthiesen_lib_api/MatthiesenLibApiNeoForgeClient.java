package dev.matthiesen.neoforge.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.utility.ClientUtils;
import dev.matthiesen.neoforge.matthiesen_lib_api.helper.MatthiesenLibClientNeoForgeHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * MatthiesenLibApiNeoForgeClient is the client-side entry point for the MatthiesenLibApi when running on the NeoForge modding platform. It is responsible for initializing client-specific components and registering necessary event listeners to ensure that the client-side API functions correctly. This class should be instantiated during the mod's client setup phase, allowing it to set up any required client-side functionality and integrate with NeoForge's event system.
 */
@SuppressWarnings("unused") // This class is instantiated reflectively by NeoForge, so it may appear unused in the codebase.
@Mod(value = MatthiesenLibApiConstants.MOD_ID, dist = Dist.CLIENT)
public class MatthiesenLibApiNeoForgeClient {
    /**
     * Initializes the client-side components of MatthiesenLibApi for NeoForge. This method should be called during the mod's client setup phase.
     * @param modBus The event bus to register client events on. This constructor is used for NeoForge's event-driven initialization process and ensures that the client-side API is properly set up when the mod is loaded on the client side.
     */
    public MatthiesenLibApiNeoForgeClient(IEventBus modBus) {
        MatthiesenLibClientNeoForgeHelper.init(modBus);
        ClientUtils.registerClientLoadRunnables();
    }
}
