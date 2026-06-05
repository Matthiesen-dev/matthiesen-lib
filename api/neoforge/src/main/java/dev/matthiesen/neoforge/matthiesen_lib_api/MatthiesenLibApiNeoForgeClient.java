package dev.matthiesen.neoforge.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MatthiesenLibApiConstants.MOD_ID, value = Dist.CLIENT)
public class MatthiesenLibApiNeoForgeClient {
    public static volatile Runnable startingRunnable = null;

    public MatthiesenLibApiNeoForgeClient() {}

    public static void setStartingRunnable(Runnable runnable) {
        startingRunnable = runnable;
    }

    @SubscribeEvent
    public static void onClientStarted(FMLClientSetupEvent event) {
        if (startingRunnable == null) return;
        startingRunnable.run();
    }
}
