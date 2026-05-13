package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonClientPlatform;
import dev.matthiesen.neoforge.matthiesen_lib.helper.NeoForgeClientRegistryHelper;

import java.util.function.Consumer;

public class NeoForgeClientPlatformService implements CommonClientPlatform {
    @Override
    public void registerMenuScreens(Consumer<ScreenRegistrar> registrationHandler) {
        NeoForgeClientRegistryHelper.registerMenuScreens(registrationHandler);
    }
}

