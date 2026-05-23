package dev.matthiesen.common.matthiesen_lib.core.interfaces;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Utility interface for registering entity and block entity renderers. This is used to avoid having to duplicate registration across platforms.
 */
public interface MatthiesenLibEntityRendererRegistrar {

    /**
     * Registers an entity renderer for a given entity type.
     *
     * @param <T>              The type of entity.
     * @param entityType       The EntityType for which the renderer is being registered.
     * @param rendererProvider The provider used to construct the renderer instance.
     */
    <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider);

    /**
     * Registers a block entity renderer for a given block entity type.
     *
     * @param <T>                  The type of block entity.
     * @param blockEntityType      The BlockEntityType for which the renderer is being registered.
     * @param rendererProvider     The provider used to construct the block entity renderer instance.
     */
    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider);

    /**
     * Registers an entity renderer using a supplier for the entity type. This is useful when the entity type is exposed as a Supplier
     * from common registration code and allows callers to use the same register(...) style in bulk registration callbacks.
     *
     * @param <T>                  The type of entity.
     * @param entityTypeSupplier   A supplier that provides the EntityType for which the renderer is being registered.
     * @param rendererProvider     The provider used to construct the renderer instance.
     */
    default <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        registerEntityRenderer(entityTypeSupplier.get(), rendererProvider);
    }

    /**
     * Registers a block entity renderer using a supplier for the block entity type. This is useful when the block entity type is exposed as a Supplier
     * from common registration code and allows callers to use the same register(...) style in bulk registration callbacks.
     *
     * @param <T>                        The type of block entity.
     * @param blockEntityTypeSupplier    A supplier that provides the BlockEntityType for which the renderer is being registered.
     * @param rendererProvider           The provider used to construct the block entity renderer instance.
     */
    default <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        registerBlockEntityRenderer(blockEntityTypeSupplier.get(), rendererProvider);
    }
}

