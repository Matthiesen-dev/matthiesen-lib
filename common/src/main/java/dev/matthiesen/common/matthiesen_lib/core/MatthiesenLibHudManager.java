package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibHudOrdering;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibHudRegistrar;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.NeoForgeVanillaGuiLayers;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Internal HUD layer registration manager used by MatthiesenLibClient.
 */
public final class MatthiesenLibHudManager {
    private static final List<HudLayerRegistration> REGISTERED_HUD_LAYERS = new CopyOnWriteArrayList<>();
    private static final Set<ResourceLocation> WARNED_MISSING_ORDER_TARGETS = new HashSet<>();
    private static final List<ResourceLocation> VANILLA_LAYER_ORDER = List.of(
            NeoForgeVanillaGuiLayers.CAMERA_OVERLAYS,
            NeoForgeVanillaGuiLayers.CROSSHAIR,
            NeoForgeVanillaGuiLayers.HOTBAR,
            NeoForgeVanillaGuiLayers.JUMP_METER,
            NeoForgeVanillaGuiLayers.EXPERIENCE_BAR,
            NeoForgeVanillaGuiLayers.PLAYER_HEALTH,
            NeoForgeVanillaGuiLayers.ARMOR_LEVEL,
            NeoForgeVanillaGuiLayers.FOOD_LEVEL,
            NeoForgeVanillaGuiLayers.VEHICLE_HEALTH,
            NeoForgeVanillaGuiLayers.AIR_LEVEL,
            NeoForgeVanillaGuiLayers.SELECTED_ITEM_NAME,
            NeoForgeVanillaGuiLayers.SPECTATOR_TOOLTIP,
            NeoForgeVanillaGuiLayers.EXPERIENCE_LEVEL,
            NeoForgeVanillaGuiLayers.EFFECTS,
            NeoForgeVanillaGuiLayers.BOSS_OVERLAY,
            NeoForgeVanillaGuiLayers.SLEEP_OVERLAY,
            NeoForgeVanillaGuiLayers.DEMO_OVERLAY,
            NeoForgeVanillaGuiLayers.DEBUG_OVERLAY,
            NeoForgeVanillaGuiLayers.SCOREBOARD_SIDEBAR,
            NeoForgeVanillaGuiLayers.OVERLAY_MESSAGE,
            NeoForgeVanillaGuiLayers.TITLE,
            NeoForgeVanillaGuiLayers.CHAT,
            NeoForgeVanillaGuiLayers.TAB_LIST,
            NeoForgeVanillaGuiLayers.SUBTITLE_OVERLAY,
            NeoForgeVanillaGuiLayers.SAVING_INDICATOR
    );

    private static volatile MatthiesenLibHudRegistrar activeRegistrar;
    private static int appliedRegistrations;
    private static boolean initialized;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatthiesenLibHudManager() {}

    /**
     * Initializes the HUD manager.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibConstants.createInfoLog("Initialized client-side HUD manager");
    }

    /**
     * Registers multiple HUD layers.
     */
    public static void registerHudLayers(Consumer<MatthiesenLibHudRegistrar> registrarConsumer) {
        registrarConsumer.accept(MatthiesenLibHudManager::registerHudLayer);
    }

    /**
     * Registers a HUD layer above all others.
     */
    public static void registerHudLayer(ResourceLocation key, LayeredDraw.Layer layer) {
        registerHudLayer(MatthiesenLibHudOrdering.AFTER, null, key, layer);
    }

    /**
     * Registers a HUD layer with explicit ordering information.
     */
    public static void registerHudLayer(MatthiesenLibHudOrdering ordering, @Nullable ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        registerHudLayerInternal(new HudLayerRegistration(ordering, other, key, layer));
    }

    /**
     * Applies all queued HUD layer registrations.
     */
    public static synchronized void applyHudLayerRegistrations(MatthiesenLibHudRegistrar registrar) {
        activeRegistrar = registrar;

        for (int i = appliedRegistrations; i < REGISTERED_HUD_LAYERS.size(); i++) {
            REGISTERED_HUD_LAYERS.get(i).apply(registrar);
        }

        appliedRegistrations = REGISTERED_HUD_LAYERS.size();
    }

    /**
     * Renders all registered HUD layers, approximating NeoForge ordering for Fabric.
     */
    public static void renderHudLayers(GuiGraphics drawContext, DeltaTracker tickCounter) {
        for (HudLayerRegistration registration : resolveRenderOrder()) {
            try {
                registration.layer().render(drawContext, tickCounter);
            } catch (Throwable throwable) {
                MatthiesenLibConstants.createErrorLog("Exception while rendering HUD layer " + registration.key(), throwable);
            }
        }
    }

    private static synchronized void registerHudLayerInternal(HudLayerRegistration registration) {
        for (HudLayerRegistration existingRegistration : REGISTERED_HUD_LAYERS) {
            if (existingRegistration.key().equals(registration.key())) {
                throw new IllegalArgumentException("Layer already registered: " + registration.key());
            }
        }

        REGISTERED_HUD_LAYERS.add(registration);

        if (activeRegistrar != null) {
            registration.apply(activeRegistrar);
            appliedRegistrations = REGISTERED_HUD_LAYERS.size();
        }
    }

    private static List<HudLayerRegistration> resolveRenderOrder() {
        List<ResourceLocation> ids = new ArrayList<>(VANILLA_LAYER_ORDER);
        List<HudLayerRegistration> customLayers = new ArrayList<>();

        for (HudLayerRegistration registration : REGISTERED_HUD_LAYERS) {
            int insertPosition;

            if (registration.other() == null) {
                insertPosition = registration.ordering() == MatthiesenLibHudOrdering.BEFORE ? 0 : ids.size();
            } else {
                int otherIndex = ids.indexOf(registration.other());
                if (otherIndex < 0) {
                    insertPosition = registration.ordering() == MatthiesenLibHudOrdering.BEFORE ? 0 : ids.size();
                    warnMissingOrderTarget(registration.other());
                } else {
                    insertPosition = otherIndex + (registration.ordering() == MatthiesenLibHudOrdering.BEFORE ? 0 : 1);
                }
            }

            ids.add(insertPosition, registration.key());
            customLayers.add(registration);
        }

        List<HudLayerRegistration> orderedLayers = new ArrayList<>();
        for (ResourceLocation id : ids) {
            for (HudLayerRegistration registration : customLayers) {
                if (registration.key().equals(id)) {
                    orderedLayers.add(registration);
                    break;
                }
            }
        }

        return orderedLayers;
    }

    private static synchronized void warnMissingOrderTarget(ResourceLocation missingTarget) {
        if (WARNED_MISSING_ORDER_TARGETS.add(missingTarget)) {
            MatthiesenLibConstants.createErrorLog("HUD layer ordering target not found: " + missingTarget + ". Rendering at edge of stack instead.");
        }
    }

    private record HudLayerRegistration(
            MatthiesenLibHudOrdering ordering,
            @Nullable ResourceLocation other,
            ResourceLocation key,
            LayeredDraw.Layer layer
    ) {
        public void apply(MatthiesenLibHudRegistrar registrar) {
            registrar.register(ordering, other, key, layer);
        }
    }
}

