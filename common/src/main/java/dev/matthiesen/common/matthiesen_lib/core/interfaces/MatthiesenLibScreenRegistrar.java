package dev.matthiesen.common.matthiesen_lib.core.interfaces;

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
public interface MatthiesenLibScreenRegistrar {
    /**
     * Registers a menu screen for a given menu type. This should be called during client initialization to ensure that the screen
     * is properly registered and can be opened when needed.
     * @param <M> The type of the menu, which must extend AbstractContainerMenu.
     * @param <S> The type of the screen, which must extend Screen and implement MenuAccess for the menu type M.
     * @param menuType The MenuType for which the screen is being registered. This identifies the type of menu that the screen will be associated with.
     * @param screenConstructor A constructor reference for creating instances of the screen. This should be a functional interface
     *                          that takes in the menu and returns a new instance of the screen, allowing the platform to create the screen when the menu is opened.
     */
    <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor);
}
