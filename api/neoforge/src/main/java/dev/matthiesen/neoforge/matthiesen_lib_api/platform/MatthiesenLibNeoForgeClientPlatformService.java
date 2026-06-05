package dev.matthiesen.neoforge.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;
import dev.matthiesen.neoforge.matthiesen_lib_api.helper.MatthiesenLibClientNeoForgeHelper;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

public class MatthiesenLibNeoForgeClientPlatformService implements MatthiesenLibClientPlatform {
    @Override
    public void onClientLoad(List<Runnable> runnable) {
        MatthiesenLibClientNeoForgeHelper.registerStartupEvent((FMLClientSetupEvent event) -> runnable.forEach(Runnable::run));
    }
}
