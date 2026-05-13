package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonClientPlatform;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Client-side initialization class for MatthiesenLib. This class is responsible for setting up any client-specific features
 * or configurations required by the library. It is called during the client initialization phase of the mod loading process,
 * allowing for the registration of client-only components such as menu screens, renderers, and other visual elements.
 */
@SuppressWarnings("unused")
public class MatthiesenLibClient {
    private static final CommonClientPlatform COMMON_CLIENT_PLATFORM =
            ServiceLoader.load(CommonClientPlatform.class).findFirst().orElseThrow();

    private static final List<Consumer<ScreenRegistrar>> PENDING_SCREEN_REGISTRATIONS = new ArrayList<>();

    private static ScreenRegistrar activeRegistrar;
    private static boolean initialized;

    /**
     * Initializes the client-side components of MatthiesenLib.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        initialized = true;
        COMMON_CLIENT_PLATFORM.registerMenuScreens(MatthiesenLibClient::bindRegistrar);
        Constants.createInfoLog("Initialized client");
    }

    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void registerMenuScreen(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        queueOrRegister(registrar -> registrar.register(menuTypeSupplier.get(), screenConstructor));
    }

    public static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void registerMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        queueOrRegister(registrar -> registrar.register(menuType, screenConstructor));
    }

    private static synchronized void queueOrRegister(Consumer<ScreenRegistrar> registration) {
        if (activeRegistrar != null) {
            registration.accept(activeRegistrar);
            return;
        }

        PENDING_SCREEN_REGISTRATIONS.add(registration);
    }

    private static synchronized void bindRegistrar(ScreenRegistrar registrar) {
        activeRegistrar = registrar;

        for (Consumer<ScreenRegistrar> registration : PENDING_SCREEN_REGISTRATIONS) {
            registration.accept(registrar);
        }

        PENDING_SCREEN_REGISTRATIONS.clear();
    }
}
