package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibEntityRendererManager;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibScreenManager;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibBlockOutlineManager;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibHudManager;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibKeybindsManager;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineContext;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineListener;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBlockOutlineRegistrar;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEntityRendererRegistrar;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibHudOrdering;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibHudRegistrar;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibKeybindMapping;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibKeybindRegistrar;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibScreenRegistrar;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Client-side facade for MatthiesenLib. Public registration methods are exposed here,
 * while internal management lives in their own classes to keep the public API clean and focused.
 */
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
        MatthiesenLibEntityRendererManager.modInitializer();
        MatthiesenLibBlockOutlineManager.modInitializer();
        MatthiesenLibHudManager.modInitializer();
        MatthiesenLibKeybindsManager.modInitializer();
    }

    // -------------------------------------------------------------------------
    // Screen registration
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Keybinds
    // -------------------------------------------------------------------------

    /**
     * Queues multiple keybinds to be registered. Safe to call at any time.
     * The platform applies registrations during its own keybinding event.
     */
    public static void registerKeybinds(Consumer<MatthiesenLibKeybindRegistrar> registrarConsumer) {
        MatthiesenLibKeybindsManager.registerKeybinds(registrarConsumer);
    }

    /**
     * Queues a keybind to be registered. Safe to call at any time.
     */
    public static void registerKeybind(String name, MatthiesenLibKeybindMapping keybind) {
        MatthiesenLibKeybindsManager.registerKeybind(name, keybind);
    }

    /**
     * Queues a keybind using only a mapping. Tick handling defaults to no-op.
     */
    public static void registerKeybind(String name, KeyMapping keyMapping) {
        MatthiesenLibKeybindsManager.registerKeybind(name, keyMapping);
    }

    /**
     * Queues a keybind using a mapping and a per-client-tick callback.
     */
    public static void registerKeybind(String name, KeyMapping keyMapping, Runnable onClientTick) {
        MatthiesenLibKeybindsManager.registerKeybind(name, keyMapping, onClientTick);
    }

    /**
     * Applies queued keybind registrations using the provided platform registrar.
     */
    public static synchronized void applyKeybindRegistrations(Consumer<KeyMapping> registrar) {
        MatthiesenLibKeybindsManager.applyKeybindRegistrations(registrar);
    }

    /**
     * Runs client tick callbacks for all registered keybind mappings.
     */
    public static void applyKeybindTicks() {
        MatthiesenLibKeybindsManager.tickKeybinds();
    }

    // -------------------------------------------------------------------------
    // Entity / block entity renderer registration
    // -------------------------------------------------------------------------

    /**
     * Queues multiple entity and block entity renderers to be registered. Safe to call at any time — the platform applies them during its own
     * registration event (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param registrarConsumer A consumer that receives a registrar helper. Call {@code registrar.registerEntityRenderer(...)} or
     *                          {@code registrar.registerBlockEntityRenderer(...)} once for each renderer you want to queue.
     */
    public static void registerEntityRenderers(Consumer<MatthiesenLibEntityRendererRegistrar> registrarConsumer) {
        MatthiesenLibEntityRendererManager.registerEntityRenderers(registrarConsumer);
    }

    /**
     * Queues an entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>                The type of entity.
     * @param entityTypeSupplier A supplier that provides the EntityType associated with the renderer. This allows for lazy evaluation,
     *                           so you can pass a reference to an entity type that may not be initialized yet.
     * @param rendererProvider   The provider used to construct the renderer instance.
     */
    public static <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        MatthiesenLibEntityRendererManager.registerEntityRenderer(entityTypeSupplier, rendererProvider);
    }

    /**
     * Queues an entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>              The type of entity.
     * @param entityType       The EntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the renderer instance.
     */
    public static <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
        MatthiesenLibEntityRendererManager.registerEntityRenderer(entityType, rendererProvider);
    }

    /**
     * Queues a block entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>                        The type of block entity.
     * @param blockEntityTypeSupplier    A supplier that provides the BlockEntityType associated with the renderer. This allows for lazy evaluation,
     *                                   so you can pass a reference to a block entity type that may not be initialized yet.
     * @param rendererProvider           The provider used to construct the block entity renderer instance.
     */
    public static <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        MatthiesenLibEntityRendererManager.registerBlockEntityRenderer(blockEntityTypeSupplier, rendererProvider);
    }

    /**
     * Queues a block entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>              The type of block entity.
     * @param blockEntityType  The BlockEntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the block entity renderer instance.
     */
    public static <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
        MatthiesenLibEntityRendererManager.registerBlockEntityRenderer(blockEntityType, rendererProvider);
    }

    /**
     * Applies all queued entity and block entity renderer registrations. Called by each platform at the correct lifecycle moment.
     * (Do not call this from an external mod.)
     *
     * @param entityRenderers      A BiConsumer used to register entity renderers on the platform.
     * @param blockEntityRenderers A BiConsumer used to register block entity renderers on the platform.
     */
    @SuppressWarnings("rawtypes")
    public static synchronized void applyEntityRendererRegistrations(BiConsumer<EntityType<? extends Entity>, EntityRendererProvider> entityRenderers,
                                                                     BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> blockEntityRenderers) {
        MatthiesenLibEntityRendererManager.applyEntityRendererRegistrations(new MatthiesenLibEntityRendererRegistrar() {
            @Override
            public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
                entityRenderers.accept(entityType, rendererProvider);
            }

            @Override
            public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
                blockEntityRenderers.accept(blockEntityType, rendererProvider);
            }
        });
    }

    // -------------------------------------------------------------------------
    // Block outline highlight listeners
    // -------------------------------------------------------------------------

    /**
     * Queues multiple block outline listeners.
     *
     * @param registrarConsumer A consumer that receives a helper to register listeners.
     */
    public static void registerBlockOutlineListeners(Consumer<MatthiesenLibBlockOutlineRegistrar> registrarConsumer) {
        MatthiesenLibBlockOutlineManager.registerBlockOutlineListeners(registrarConsumer);
    }

    /**
     * Queues a block outline listener.
     *
     * @param listener The listener to invoke when the player highlights a block.
     *
     * <p>Example:</p>
     * <pre>{@code
     * MatthiesenLibClient.registerBlockOutlineListener(context -> {
     *     BlockPos basePos = MyClientLogic.resolveBasePos(context.level(), context.blockHitResult().getBlockPos());
     *     if (basePos == null) {
     *         return true; // Keep vanilla outline when there is no override target.
     *     }
     *
     *     BlockState baseState = context.level().getBlockState(basePos);
     *     VoxelShape shape = baseState.getShape(context.level(), basePos);
     *
     *     double x = basePos.getX() - context.camera().getPosition().x();
     *     double y = basePos.getY() - context.camera().getPosition().y();
     *     double z = basePos.getZ() - context.camera().getPosition().z();
     *
     *     LevelRenderer.renderVoxelShape(
     *             context.poseStack(),
     *             context.multiBufferSource().getBuffer(RenderType.lines()),
     *             shape,
     *             x, y, z,
     *             0.0F, 0.0F, 0.0F, 0.4F,
     *             false
     *     );
     *
     *     return false; // Cancel vanilla box because custom outline was drawn.
     * });
     * }</pre>
     */
    public static void registerBlockOutlineListener(MatthiesenLibBlockOutlineListener listener) {
        MatthiesenLibBlockOutlineManager.registerBlockOutlineListener(listener);
    }

    /**
     * Applies registered block outline listeners to event data.
     *
     * @param level             The current client level.
     * @param blockHitResult    The targeted block hit result.
     * @param poseStack         The active pose stack.
     * @param camera            The active camera.
     * @param multiBufferSource The active render buffer source.
     * @return {@code true} to continue with vanilla outline rendering, {@code false} to cancel it.
     */
    public static boolean applyBlockOutlineListeners(ClientLevel level,
                                                     BlockHitResult blockHitResult,
                                                     PoseStack poseStack,
                                                     Camera camera,
                                                     MultiBufferSource multiBufferSource) {
        return MatthiesenLibBlockOutlineManager.fireBlockOutlineEvent(new MatthiesenLibBlockOutlineContext(
                level,
                blockHitResult,
                poseStack,
                camera,
                multiBufferSource
        ));
    }

    // -------------------------------------------------------------------------
    // HUD Rendering
    // -------------------------------------------------------------------------

    /**
     * Queues multiple HUD layers to be registered.
     * @param registrarConsumer A consumer that accepts a {@link MatthiesenLibHudRegistrar} to register layers with. This is typically a method
     *
     * <p>Example:</p>
     * <pre>{@code
     * MatthiesenLibClient.registerHudLayers(registrar -> {
     *     registrar.registerBelow(
     *             NeoForgeVanillaGuiLayers.CHAT,
     *             ResourceLocation.fromNamespaceAndPath("examplemod", "status_hud"),
     *             (guiGraphics, deltaTracker) -> {
     *                 // Your HUD rendering logic
     *             }
     *     );
     *
     *     registrar.registerAboveAll(
     *             ResourceLocation.fromNamespaceAndPath("examplemod", "debug_hud"),
     *             (guiGraphics, deltaTracker) -> {
     *                 // Another HUD layer
     *             }
     *     );
     * });
     * }</pre>
     */
    public static void registerHudLayers(Consumer<MatthiesenLibHudRegistrar> registrarConsumer) {
        MatthiesenLibHudManager.registerHudLayers(registrarConsumer);
    }

    /**
     * Queues a HUD layer that renders above all others.
     * @param key A unique id for the layer, used for ordering and debugging.
     * @param layer The layer implementation.
     *
     * <p>Example:</p>
     * <pre>{@code
     * MatthiesenLibClient.registerHudLayer(
     *         ResourceLocation.fromNamespaceAndPath("examplemod", "simple_hud"),
     *         (guiGraphics, deltaTracker) -> {
     *             // Render a simple always-on-top HUD layer
     *         }
     * );
     * }</pre>
     */
    public static void registerHudLayer(ResourceLocation key, LayeredDraw.Layer layer) {
        MatthiesenLibHudManager.registerHudLayer(key, layer);
    }

    /**
     * Queues a HUD layer with explicit ordering information.
     * @param key A unique id for the layer, used for ordering and debugging.
     * @param other The layer id to order against, or {@code null} to target the top or bottom of the stack depending on {@code ordering}.
     * @param ordering Whether the new layer should render before or after {@code other}, or whether it should target the top or bottom of the stack if {@code other} is null.
     * @param layer The layer implementation.
     *
     * <p>Example:</p>
     * <pre>{@code
     * MatthiesenLibClient.registerHudLayer(
     *         MatthiesenLibHudOrdering.BEFORE,
     *         NeoForgeVanillaGuiLayers.CHAT,
     *         ResourceLocation.fromNamespaceAndPath("examplemod", "chat_background_hud"),
     *         (guiGraphics, deltaTracker) -> {
     *             // Render a layer before chat
     *         }
     * );
     * }</pre>
     */
    public static void registerHudLayer(MatthiesenLibHudOrdering ordering, @Nullable ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        MatthiesenLibHudManager.registerHudLayer(ordering, other, key, layer);
    }

    /**
     * Applies queued HUD layer registrations to a platform registrar.
     * @param registrar The platform-specific registrar to apply HUD layer registrations with. This is typically provided by the platform's HUD layer registration event (e.g. RegisterGuiLayersEvent for NeoForge).
     */
    public static synchronized void applyHudLayerRegistrations(MatthiesenLibHudRegistrar registrar) {
        MatthiesenLibHudManager.applyHudLayerRegistrations(registrar);
    }

    /**
     * Renders registered HUD layers for Fabric's HUD callback.
     * @param drawContext The rendering context to draw with.
     * @param tickCounter The tick counter to pass to layers.
     */
    public static void applyFabricHudRendering(GuiGraphics drawContext, DeltaTracker tickCounter) {
        MatthiesenLibHudManager.renderHudLayers(drawContext, tickCounter);
    }
}
