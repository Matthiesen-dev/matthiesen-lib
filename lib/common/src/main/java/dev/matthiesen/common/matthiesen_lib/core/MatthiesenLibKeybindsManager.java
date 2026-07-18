package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibKeybindMapping;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibKeybindRegistrar;
import net.minecraft.client.KeyMapping;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class MatthiesenLibKeybindsManager {
    private static final List<KeybindRegistration> REGISTERED_KEYBINDS = new CopyOnWriteArrayList<>();
    private static volatile Consumer<KeyMapping> activeRegistrar;
    private static int appliedRegistrations;
    private static boolean initialized;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatthiesenLibKeybindsManager() {}

    /**
     * Initializes the keybind manager. This method is idempotent and will only perform initialization once, even if called multiple times.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibConstants.createInfoLog("Initialized client-side keybind manager");
    }

    public static void registerKeybinds(Consumer<MatthiesenLibKeybindRegistrar> registrarConsumer) {
        registrarConsumer.accept(MatthiesenLibKeybindsManager::registerKeybind);
    }

    public static void registerKeybind(String name, MatthiesenLibKeybindMapping keybind) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(keybind, "keybind");
        registerKeybindInternal(new KeybindRegistration(name, keybind));
    }

    public static void registerKeybind(String name, KeyMapping keyMapping) {
        registerKeybind(name, keyMapping, () -> {});
    }

    public static void registerKeybind(String name, KeyMapping keyMapping, Runnable onClientTick) {
        Objects.requireNonNull(keyMapping, "keyMapping");
        Objects.requireNonNull(onClientTick, "onClientTick");
        registerKeybind(name, new MatthiesenLibKeybindMapping() {
            @Override
            public KeyMapping getKeybind() {
                return keyMapping;
            }

            @Override
            public void onClientTick() {
                onClientTick.run();
            }
        });
    }

    public static synchronized void applyKeybindRegistrations(Consumer<KeyMapping> registrar) {
        activeRegistrar = registrar;

        for (int i = appliedRegistrations; i < REGISTERED_KEYBINDS.size(); i++) {
            REGISTERED_KEYBINDS.get(i).apply(registrar);
        }

        appliedRegistrations = REGISTERED_KEYBINDS.size();
    }

    public static void tickKeybinds() {
        for (KeybindRegistration registration : REGISTERED_KEYBINDS) {
            try {
                registration.keybind().onClientTick();
            } catch (Throwable throwable) {
                MatthiesenLibConstants.createErrorLog("Exception while handling keybind tick for " + registration.name(), throwable);
            }
        }
    }

    private static synchronized void registerKeybindInternal(KeybindRegistration registration) {
        for (KeybindRegistration existingRegistration : REGISTERED_KEYBINDS) {
            if (existingRegistration.name().equals(registration.name())) {
                throw new IllegalArgumentException("Keybind already registered: " + registration.name());
            }
        }

        REGISTERED_KEYBINDS.add(registration);

        if (activeRegistrar != null) {
            registration.apply(activeRegistrar);
            appliedRegistrations = REGISTERED_KEYBINDS.size();
        }
    }

    private record KeybindRegistration(String name, MatthiesenLibKeybindMapping keybind) {
        public void apply(Consumer<KeyMapping> registrar) {
            registrar.accept(keybind.getKeybind());
        }
    }
}
