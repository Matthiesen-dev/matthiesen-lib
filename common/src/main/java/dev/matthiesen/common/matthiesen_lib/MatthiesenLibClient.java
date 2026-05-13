package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;
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

    private static boolean initialized;

    /**
     * Initializes the client-side components of MatthiesenLib. (Do not run this from an external mod.)
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        Constants.createInfoLog("Initialized client");
    }

    /**
     * Queues a screen to be registered. Safe to call at any time — the platform applies it during its
     * own registration event (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     */
    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        REGISTERED_SCREENS.add(new ScreenEntry<>(menuTypeSupplier.get(), screenConstructor));
    }

    /**
     * Queues a screen to be registered. Safe to call at any time — the platform applies it during its
     * own registration event (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     */
    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        REGISTERED_SCREENS.add(new ScreenEntry<>(menuType, screenConstructor));
    }

    /**
     * Applies all queued screen registrations to the provided registrar. Called by each platform at
     * the correct lifecycle moment.
     */
    public static void applyScreenRegistrations(ScreenRegistrar registrar) {
        for (ScreenEntry<?, ?> entry : REGISTERED_SCREENS) {
            entry.apply(registrar);
        }
    }

    private record ScreenEntry<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>(
            MenuType<? extends M> menuType,
            MenuScreens.ScreenConstructor<M, S> screenConstructor
    ) {
        void apply(ScreenRegistrar registrar) {
            registrar.register(menuType, screenConstructor);
        }
    }
}
