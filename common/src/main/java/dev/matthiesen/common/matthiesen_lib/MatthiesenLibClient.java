package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.client.MatthiesenLibScreenManager;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibScreenRegistrar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Client-side facade for MatthiesenLib. Public registration methods are exposed here,
 * while internal screen queue/state management lives in MatthiesenLibScreenManager.
 */
@SuppressWarnings("unused")
public class MatthiesenLibClient {
    /**
     * Default constructor for the MatthiesenLibClient class. No initialization is required as setup is handled in the modInitializer method.
     */
    private MatthiesenLibClient() {}

    /**
     * Initializes the client-side components of MatthiesenLib. (Do not run this from an external mod.)
     */
    public static synchronized void modInitializer() {
        MatthiesenLibScreenManager.modInitializer();
    }

    /**
     * Queues multiple screens to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     * @param registrarConsumer A consumer that receives a register helper. Call {@code register.register(...)} once for each menu screen you want to queue.
     */
    public static void registerMenuScreens(Consumer<MatthiesenLibScreenRegistrar> registrarConsumer) {
        MatthiesenLibScreenManager.registerMenuScreens(registrarConsumer);
    }

    /**
     * Queues a screen to be registered. Safe to call at any time — the platform applies it during its
     * own registration event (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     *
     * @param <M> The menu type for the screen.
     * @param <S> The screen type.
     * @param menuTypeSupplier A supplier that provides the MenuType associated with the screen. This allows for lazy evaluation, so you can
     *                         pass a reference to a menu type that may not be initialized yet.
     * @param screenConstructor The constructor for the screen, which takes the menu and the player's inventory as parameters. This is used to
     *                          create the screen instance when the menu is opened.
     */
    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        MatthiesenLibScreenManager.registerMenuScreen(menuTypeSupplier, screenConstructor);
    }

    /**
     * Queues a screen to be registered. Safe to call at any time — the platform applies it during its
     * own registration event (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     *
     * @param <M> The menu type for the screen.
     * @param <S> The screen type.
     * @param menuType The MenuType associated with the screen. This is used to identify which menu the screen should be opened for, and it should
     *                 match the menu type that is used for the screen's container.
     * @param screenConstructor The constructor for the screen, which takes the menu and the player's inventory as parameters. This is used to create
     *                          the screen instance when the menu is opened.
     */
    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        MatthiesenLibScreenManager.registerMenuScreen(menuType, screenConstructor);
    }

    /**
     * Applies all queued screen registrations to the provided registrar. Called by each platform at
     * the correct lifecycle moment.
     *
     * @param registrar The ScreenRegistrar provided by the platform, which is used to register screens with their associated menu types. This should be
     *                  called during the platform's screen registration event, and it will apply all queued screen registrations to the game.
     */
    public static synchronized void applyScreenRegistrations(MatthiesenLibScreenRegistrar registrar) {
        MatthiesenLibScreenManager.applyScreenRegistrations(registrar);
    }
}
