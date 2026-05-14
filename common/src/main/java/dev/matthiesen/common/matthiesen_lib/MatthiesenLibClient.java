package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibScreenRegistrar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Client-side initialization class for MatthiesenLib. Stores screen registrations in a static list
 * so each platform can apply them at the correct lifecycle stage.
 */
@SuppressWarnings("unused")
public class MatthiesenLibClient {
    private static final List<ScreenEntry<?, ?>> REGISTERED_SCREENS = new CopyOnWriteArrayList<>();
    private static volatile MatthiesenLibScreenRegistrar activeRegistrar;
    private static int appliedRegistrations;

    private static boolean initialized;

    /**
     * Default constructor for the MatthiesenLibClient class. No initialization is required as setup is handled in the modInitializer method.
     */
    public MatthiesenLibClient() {}

    /**
     * Initializes the client-side components of MatthiesenLib. (Do not run this from an external mod.)
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibConstants.createInfoLog("Initialized client");
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
        registerMenuScreenInternal(new ScreenEntry<>(menuTypeSupplier.get(), screenConstructor));
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
        registerMenuScreenInternal(new ScreenEntry<>(menuType, screenConstructor));
    }

    /**
     * Applies all queued screen registrations to the provided registrar. Called by each platform at
     * the correct lifecycle moment.
     *
     * @param registrar The ScreenRegistrar provided by the platform, which is used to register screens with their associated menu types. This should be
     *                  called during the platform's screen registration event, and it will apply all queued screen registrations to the game.
     */
    public static synchronized void applyScreenRegistrations(MatthiesenLibScreenRegistrar registrar) {
        activeRegistrar = registrar;

        for (int i = appliedRegistrations; i < REGISTERED_SCREENS.size(); i++) {
            REGISTERED_SCREENS.get(i).apply(registrar);
        }

        appliedRegistrations = REGISTERED_SCREENS.size();
    }

    /**
     * Internal method to register a screen entry. This method is synchronized to ensure thread safety when adding entries to the list and applying registrations.
     * @param entry The ScreenEntry to register, which contains the menu type and screen constructor for the screen. This entry will be added to the list of registered
     *              screens, and if the active registrar is already set, it will be applied immediately.
     */
    private static synchronized void registerMenuScreenInternal(ScreenEntry<?, ?> entry) {
        REGISTERED_SCREENS.add(entry);

        // Fabric can register screens immediately once client init has provided a registrar.
        if (activeRegistrar != null) {
            entry.apply(activeRegistrar);
            appliedRegistrations = REGISTERED_SCREENS.size();
        }
    }

    /**
     * A record to store information about a screen registration, including the menu type and the screen constructor. This is used to queue screen
     * registrations before they are applied to the game, allowing for lazy evaluation of menu types and constructors. The apply method is used to
     * register the screen with the provided ScreenRegistrar when the platform calls for it.
     *
     * @param menuType The MenuType associated with the screen. This is used to identify which menu the screen should be opened for, and it should
     *                 match the menu type that is used for the screen's container.
     * @param screenConstructor The constructor for the screen, which takes the menu and the player's inventory as parameters. This is used to create
     *                          the screen instance when the menu is opened.
     * @param <M> The menu type for the screen. This should be a subclass of AbstractContainerMenu, and it should match the menu type that is used for
     *           the screen's container. This allows the screen to access the menu's inventory and other data as needed.
     * @param <S> The screen type, which must implement MenuAccess<M>. This allows the screen to access the menu's inventory and other data as needed,
     *           and it ensures that the screen is compatible with the menu type that it is associated with.
     */
    private record ScreenEntry<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>(
            MenuType<? extends M> menuType,
            MenuScreens.ScreenConstructor<M, S> screenConstructor
    ) {
        /**
         * Registers the screen with the provided ScreenRegistrar. This should be called during the platform's screen registration event, and it will
         * register the screen with its associated menu type so that it can be opened when the menu is opened.
         *
         * @param registrar The ScreenRegistrar provided by the platform, which is used to register screens with their associated menu types. This should
         *                  be called during the platform's screen registration event, and it will register the screen with the game so that it can be opened
         *                  when the associated menu is opened.
         */
        void apply(MatthiesenLibScreenRegistrar registrar) {
            registrar.register(menuType, screenConstructor);
        }
    }
}
