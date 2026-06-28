package dev.matthiesen.common.matthiesen_lib.abstracts;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.*;
import dev.matthiesen.common.matthiesen_lib_api.abstracts.AbstractCommonMod;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Client side mod class. This class is used to register client side features such as renderers, screens, and other client side features.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonClientMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final AbstractCommonMod SERVER_MOD;

    /**
     * Constructor for the AbstractCommonClientMod class.
     *
     * @param mod The Server-side mod instance
     */
    public AbstractCommonClientMod(AbstractCommonMod mod) {
        this.MOD_ID = mod.getModId();
        this.MOD_NAME = mod.getModName() + " (client)";
        this.SERVER_MOD = mod;
        this.LOGGER = LogManager.getLogger(this.MOD_NAME);
    }

    /**
     * Initializer for the client mod. This method is called during the mod initialization phase.
     */
    public abstract void initialize();

    /**
     * Get the mod's ID
     * @return The mod's ID
     */
    public String getModId() {
        return MOD_ID;
    }

    /**
     * Get the mod's name
     * @return The mod's name
     */
    public String getModName() {
        return MOD_NAME;
    }

    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * Send an info log message using the mod's logger.
     * @param message The message to log
     */
    public void createInfoLog(String message) {
        getLogger().info(message);
    }

    /**
     * Send a warning log message using the mod's logger.
     * @param message The message to log
     */
    public void createWarnLog(String message) {
        getLogger().warn(message);
    }

    /**
     * Send an error log message using the mod's logger.
     * @param message The message to log
     */
    public void createErrorLog(String message) {
        getLogger().error(message);
    }

    /**
     * Send an error log message using the mod's logger, and track the error with the metrics system if a metrics token is provided
     * @param message The message to log
     * @param throwable The error to log and track
     */
    public void createErrorLog(String message, Throwable throwable) {
        SERVER_MOD.trackError(throwable);
        getLogger().error(message, throwable);
    }

    // ------- UTILS -------


    // -------------------------------------------------------------------------
    // Screen registration
    // -------------------------------------------------------------------------

    /**
     * Queues multiple screens to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: RegisterMenuScreensEvent).
     * @param registrarConsumer A consumer that receives a register helper. Call {@code register.register(...)} once for each menu screen you want to queue.
     */
    public void registerMenuScreens(Consumer<MatthiesenLibScreenRegistrar> registrarConsumer) {
        MatthiesenLibClient.registerMenuScreens(registrarConsumer);
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
    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        MatthiesenLibClient.registerMenuScreen(menuTypeSupplier, screenConstructor);
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
    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void registerMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        MatthiesenLibClient.registerMenuScreen(menuType, screenConstructor);
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
    public void registerEntityRenderers(Consumer<MatthiesenLibEntityRendererRegistrar> registrarConsumer) {
        MatthiesenLibClient.registerEntityRenderers(registrarConsumer);
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
    public <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        MatthiesenLibClient.registerEntityRenderer(entityTypeSupplier, rendererProvider);
    }

    /**
     * Queues an entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>              The type of entity.
     * @param entityType       The EntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the renderer instance.
     */
    public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
        MatthiesenLibClient.registerEntityRenderer(entityType, rendererProvider);
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
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        MatthiesenLibClient.registerBlockEntityRenderer(blockEntityTypeSupplier, rendererProvider);
    }

    /**
     * Queues a block entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>              The type of block entity.
     * @param blockEntityType  The BlockEntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the block entity renderer instance.
     */
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
        MatthiesenLibClient.registerBlockEntityRenderer(blockEntityType, rendererProvider);
    }

    // -------------------------------------------------------------------------
    // Block outline highlight listeners
    // -------------------------------------------------------------------------

    /**
     * Queues multiple block outline listeners.
     *
     * @param registrarConsumer A consumer that receives a helper to register listeners.
     */
    public void registerBlockOutlineListeners(Consumer<MatthiesenLibBlockOutlineRegistrar> registrarConsumer) {
        MatthiesenLibClient.registerBlockOutlineListeners(registrarConsumer);
    }

    /**
     * Queues a block outline listener.
     *
     * @param listener The listener to invoke when the player highlights a block.
     *
     * <p>Example:</p>
     * <pre>{@code
     * INSTANCE.registerBlockOutlineListener(context -> {
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
    public void registerBlockOutlineListener(MatthiesenLibBlockOutlineListener listener) {
        MatthiesenLibClient.registerBlockOutlineListener(listener);
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
     * INSTANCE.registerHudLayers(registrar -> {
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
    public void registerHudLayers(Consumer<MatthiesenLibHudRegistrar> registrarConsumer) {
        MatthiesenLibClient.registerHudLayers(registrarConsumer);
    }

    /**
     * Queues a HUD layer that renders above all others.
     * @param key A unique id for the layer, used for ordering and debugging.
     * @param layer The layer implementation.
     *
     * <p>Example:</p>
     * <pre>{@code
     * INSTANCE.registerHudLayer(
     *         ResourceLocation.fromNamespaceAndPath("examplemod", "simple_hud"),
     *         (guiGraphics, deltaTracker) -> {
     *             // Render a simple always-on-top HUD layer
     *         }
     * );
     * }</pre>
     */
    public void registerHudLayer(ResourceLocation key, LayeredDraw.Layer layer) {
        MatthiesenLibClient.registerHudLayer(key, layer);
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
     * INSTANCE.registerHudLayer(
     *         MatthiesenLibHudOrdering.BEFORE,
     *         NeoForgeVanillaGuiLayers.CHAT,
     *         ResourceLocation.fromNamespaceAndPath("examplemod", "chat_background_hud"),
     *         (guiGraphics, deltaTracker) -> {
     *             // Render a layer before chat
     *         }
     * );
     * }</pre>
     */
    public void registerHudLayer(MatthiesenLibHudOrdering ordering, @Nullable ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        MatthiesenLibClient.registerHudLayer(ordering, other, key, layer);
    }
}
