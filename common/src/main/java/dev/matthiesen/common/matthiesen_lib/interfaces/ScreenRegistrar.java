package dev.matthiesen.common.matthiesen_lib.interfaces;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * Utility interface for registering menu screens. This is used to avoid having to duplicate registration across platforms.
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface ScreenRegistrar {
    <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor);
}
