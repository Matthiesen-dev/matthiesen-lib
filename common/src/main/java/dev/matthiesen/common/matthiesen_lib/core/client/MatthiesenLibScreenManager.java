package dev.matthiesen.common.matthiesen_lib.core.client;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibScreenRegistrar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Internal screen registration manager used by MatthiesenLibClient.
 */
public class MatthiesenLibScreenManager {
	private static final List<ScreenRegistration> REGISTERED_SCREENS = new CopyOnWriteArrayList<>();
	private static volatile MatthiesenLibScreenRegistrar activeRegistrar;
	private static int appliedRegistrations;
	private static boolean initialized;

    /**
     * Private constructor to prevent instantiation of this utility class. All methods are static, and there is no need to create an instance of this class.
     */
	private MatthiesenLibScreenManager() {}

    /**
     * Initializes the screen manager. This method is idempotent and will only perform initialization once, even if called multiple times.
     */
	public static synchronized void modInitializer() {
		if (initialized) {
			return;
		}

		initialized = true;
		MatthiesenLibConstants.createInfoLog("Initialized client-side screen manager");
	}

    /**
     * Queues multiple screens to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     * @param registrarConsumer A consumer that receives a register helper. Call {@code register.register(...)} once for each menu screen you want to queue.
     */
	public static void registerMenuScreens(Consumer<MatthiesenLibScreenRegistrar> registrarConsumer) {
		registrarConsumer.accept(new MatthiesenLibScreenRegistrar() {
			@Override
			public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
			void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
				registerMenuScreenInternal(new ScreenEntry<>(menuType, screenConstructor));
			}
		});
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
		registerMenuScreenInternal(registrar -> registrar.register(menuTypeSupplier, screenConstructor));
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
     * Internal helper method to register a screen. This adds the registration to the list of registered screens and applies it immediately if the registrar is already active.
     * @param registration The screen registration to be added. This should be an instance of a class that implements the ScreenRegistration interface, which defines how to apply the registration to the registrar.
     */
	private static synchronized void registerMenuScreenInternal(ScreenRegistration registration) {
		REGISTERED_SCREENS.add(registration);

		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedRegistrations = REGISTERED_SCREENS.size();
		}
	}

    /**
     * A functional interface representing a screen registration. This interface defines a single method, apply, which takes a ScreenRegistrar and applies the registration to it. Implementations of this interface should contain the logic to register a screen with the given registrar, using the appropriate menu type and screen constructor.
     */
	@FunctionalInterface
	private interface ScreenRegistration {
        /**
         * Applies this screen registration to the provided registrar. This method should contain the logic to register the screen with the given registrar, using the appropriate menu type and screen constructor.
         * @param registrar The ScreenRegistrar provided by the platform, which is used to register screens with their associated menu types. This should be called during the platform's screen registration event, and it will apply this screen registration to the game.
         */
		void apply(MatthiesenLibScreenRegistrar registrar);
	}

    /**
     * A record class representing a screen registration entry. This class implements the ScreenRegistration interface and contains the necessary information to register a screen, including the menu type and the screen constructor. When the apply method is called, it uses the provided registrar to register the screen with the specified menu type and constructor.
     * @param menuType The MenuType associated with the screen. This is used to identify which menu the screen should be opened for, and it should match the menu type that is used for the screen's container.
     * @param screenConstructor The constructor for the screen, which takes the menu and the player's inventory as parameters. This is used to create the screen instance when the menu is opened.
     * @param <M> The menu type for the screen, which must extend AbstractContainerMenu. This is used to ensure that the screen is associated with a valid menu type that can be opened in the game.
     * @param <S> The screen type, which must extend Screen and implement MenuAccess for the menu type M. This is used to ensure that the screen can properly access the menu's inventory and other data when it is opened.
     */
	private record ScreenEntry<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>(
			MenuType<? extends M> menuType,
			MenuScreens.ScreenConstructor<M, S> screenConstructor
	) implements ScreenRegistration {
		@Override
		public void apply(MatthiesenLibScreenRegistrar registrar) {
			registrar.register(menuType, screenConstructor);
		}
	}
}
