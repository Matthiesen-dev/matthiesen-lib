package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEntityRendererRegistrar;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Internal entity renderer registration manager used by MatthiesenLibClient.
 */
public class MatthiesenLibEntityRendererManager {
    private static final List<RendererRegistration> REGISTERED_ENTITY_RENDERERS = new CopyOnWriteArrayList<>();
    private static final List<RendererRegistration> REGISTERED_BLOCK_ENTITY_RENDERERS = new CopyOnWriteArrayList<>();

    private static volatile MatthiesenLibEntityRendererRegistrar activeRegistrar;
    private static int appliedEntityRegistrations;
    private static int appliedBlockEntityRegistrations;
    private static boolean initialized;

    /**
     * Private constructor to prevent instantiation of this utility class. All methods are static, and there is no need to create an instance of this class.
     */
    private MatthiesenLibEntityRendererManager() {}

    /**
     * Initializes the entity renderer manager. This method is idempotent and will only perform initialization once, even if called multiple times.
     */
    public static synchronized void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibConstants.createInfoLog("Initialized client-side entity renderer manager");
    }

    /**
     * Queues multiple entity and block entity renderers to be registered. Safe to call at any time — the platform applies them during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param registrarConsumer A consumer that receives a registrar helper. Call {@code registrar.registerEntityRenderer(...)} or
     *                          {@code registrar.registerBlockEntityRenderer(...)} once for each renderer you want to queue.
     */
    public static void registerEntityRenderers(Consumer<MatthiesenLibEntityRendererRegistrar> registrarConsumer) {
        registrarConsumer.accept(new MatthiesenLibEntityRendererRegistrar() {
            @Override
            public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
                registerEntityRendererInternal(new EntityRendererEntry<>(entityType, rendererProvider));
            }

            @Override
            public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
                registerBlockEntityRendererInternal(new BlockEntityRendererEntry<>(blockEntityType, rendererProvider));
            }
        });
    }

    /**
     * Queues an entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>              The type of entity.
     * @param entityTypeSupplier A supplier that provides the EntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the renderer instance.
     */
    public static <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        registerEntityRendererInternal(registrar -> registrar.registerEntityRenderer(entityTypeSupplier, rendererProvider));
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
        registerEntityRendererInternal(new EntityRendererEntry<>(entityType, rendererProvider));
    }

    /**
     * Queues a block entity renderer to be registered. Safe to call at any time — the platform applies it during its own registration event
     * (Fabric: onInitializeClient, NeoForge: EntityRenderersEvent.RegisterRenderers).
     *
     * @param <T>                  The type of block entity.
     * @param blockEntityTypeSupplier A supplier that provides the BlockEntityType associated with the renderer.
     * @param rendererProvider     The provider used to construct the block entity renderer instance.
     */
    public static <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        registerBlockEntityRendererInternal(registrar -> registrar.registerBlockEntityRenderer(blockEntityTypeSupplier, rendererProvider));
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
        registerBlockEntityRendererInternal(new BlockEntityRendererEntry<>(blockEntityType, rendererProvider));
    }

    /**
     * Applies all queued entity and block entity renderer registrations to the provided registrar. Called by each platform at
     * the correct lifecycle moment.
     *
     * @param registrar The EntityRendererRegistrar provided by the platform, used to register renderers. This should be
     *                  called during the platform's renderer registration event.
     */
    public static synchronized void applyEntityRendererRegistrations(MatthiesenLibEntityRendererRegistrar registrar) {
        activeRegistrar = registrar;

        for (int i = appliedEntityRegistrations; i < REGISTERED_ENTITY_RENDERERS.size(); i++) {
            REGISTERED_ENTITY_RENDERERS.get(i).apply(registrar);
        }
        appliedEntityRegistrations = REGISTERED_ENTITY_RENDERERS.size();

        for (int i = appliedBlockEntityRegistrations; i < REGISTERED_BLOCK_ENTITY_RENDERERS.size(); i++) {
            REGISTERED_BLOCK_ENTITY_RENDERERS.get(i).apply(registrar);
        }
        appliedBlockEntityRegistrations = REGISTERED_BLOCK_ENTITY_RENDERERS.size();
    }

    /**
     * Internal helper to queue an entity renderer registration, applying it immediately if the registrar is already active.
     */
    private static synchronized void registerEntityRendererInternal(RendererRegistration registration) {
        REGISTERED_ENTITY_RENDERERS.add(registration);

        if (activeRegistrar != null) {
            registration.apply(activeRegistrar);
            appliedEntityRegistrations = REGISTERED_ENTITY_RENDERERS.size();
        }
    }

    /**
     * Internal helper to queue a block entity renderer registration, applying it immediately if the registrar is already active.
     */
    private static synchronized void registerBlockEntityRendererInternal(RendererRegistration registration) {
        REGISTERED_BLOCK_ENTITY_RENDERERS.add(registration);

        if (activeRegistrar != null) {
            registration.apply(activeRegistrar);
            appliedBlockEntityRegistrations = REGISTERED_BLOCK_ENTITY_RENDERERS.size();
        }
    }

    /**
     * A functional interface representing a renderer registration.
     */
    @FunctionalInterface
    private interface RendererRegistration {
        /**
         * Applies this renderer registration to the provided registrar.
         *
         * @param registrar The EntityRendererRegistrar provided by the platform.
         */
        void apply(MatthiesenLibEntityRendererRegistrar registrar);
    }

    /**
     * A record representing an entity renderer registration entry.
     *
     * @param entityType       The EntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the renderer.
     * @param <T>              The type of entity.
     */
    private record EntityRendererEntry<T extends Entity>(
            EntityType<? extends T> entityType,
            EntityRendererProvider<T> rendererProvider
    ) implements RendererRegistration {
        @Override
        public void apply(MatthiesenLibEntityRendererRegistrar registrar) {
            registrar.registerEntityRenderer(entityType, rendererProvider);
        }
    }

    /**
     * A record representing a block entity renderer registration entry.
     *
     * @param blockEntityType  The BlockEntityType associated with the renderer.
     * @param rendererProvider The provider used to construct the block entity renderer.
     * @param <T>              The type of block entity.
     */
    private record BlockEntityRendererEntry<T extends BlockEntity>(
            BlockEntityType<? extends T> blockEntityType,
            BlockEntityRendererProvider<T> rendererProvider
    ) implements RendererRegistration {
        @Override
        public void apply(MatthiesenLibEntityRendererRegistrar registrar) {
            registrar.registerBlockEntityRenderer(blockEntityType, rendererProvider);
        }
    }
}
