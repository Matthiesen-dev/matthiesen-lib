package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonClientPlatform;
import net.minecraft.client.gui.screens.MenuScreens;

import java.util.function.Consumer;

public class FabricClientPlatformService implements CommonClientPlatform {
    @Override
    public void registerMenuScreens(Consumer<ScreenRegistrar> registrationHandler) {
        registrationHandler.accept(MenuScreens::register);
    }
}

