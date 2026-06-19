package dev.matthiesen.fabric.matthiesen_lib_api.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibModContainer;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibPlatform;
import dev.matthiesen.fabric.matthiesen_lib_api.MatthiesenLibApiFabric;
import dev.matthiesen.fabric.matthiesen_lib_api.permission.MatthiesenLibFabricMatthiesenLibPermissionValidator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * MatthiesenLibFabricPlatformService is the implementation of the MatthiesenLibPlatform interface for the Fabric mod loader. It provides
 * methods for registering blocks, items, sounds, creative tabs, criteria triggers, stats, menu types, data component
 * types, and entity effects using the Fabric API. It also includes utilities for checking if a mod is loaded and if
 * the environment is a development environment. This class serves as the bridge between the common code in MatthiesenLib
 * and the specific implementation details of the Fabric platform.
 */
public class MatthiesenLibFabricPlatformService implements MatthiesenLibPlatform {
    /**
     * Default constructor for the FabricPlatformService. No initialization is required as all methods are stateless and rely on the Fabric API for registration and utilities.
     */
    public MatthiesenLibFabricPlatformService() {}

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerSupplier(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return registerSupplier(BuiltInRegistries.BLOCK, id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return registerSupplier(BuiltInRegistries.ITEM, id, item);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return registerSupplier(BuiltInRegistries.SOUND_EVENT, id, sound);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return registerSupplier(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return registerSupplier(BuiltInRegistries.TRIGGER_TYPES, id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return registerSupplier(BuiltInRegistries.CUSTOM_STAT, id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return registerSupplier(BuiltInRegistries.MENU, id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return registerSupplier(BuiltInRegistries.DATA_COMPONENT_TYPE, id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return registerSupplier(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, name, codec);
    }

    @Override
    public void registerPermissionValidator() {
        if (FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0")) {
            MatthiesenLibApi.setPermissionValidator(new MatthiesenLibFabricMatthiesenLibPermissionValidator());
        }
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public MatthiesenLibModContainer getModContainer(String modId) {
        var fabricModContainer = FabricLoader.getInstance().getModContainer(modId);
        return fabricModContainer.map(modContainer -> new MatthiesenLibModContainer() {
            @Override
            public String getModName() {
                return modContainer.getMetadata().getName();
            }

            @Override
            public String getModVersion() {
                return modContainer.getMetadata().getVersion().getFriendlyString();
            }

            @Override
            public String getPlatform() {
                return Platform.FABRIC.getLabel();
            }

            @Override
            public String getPlatformId() {
                return Platform.FABRIC.getModId();
            }
        }).orElse(null);
    }

    @Override
    public Path getModConfig(String dir, String file) {
        return FabricLoader.getInstance().getConfigDir().resolve(dir).resolve(file);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public ENVIRONMENT getEnvironmentType() {
        return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT ? ENVIRONMENT.CLIENT : ENVIRONMENT.SERVER;
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public MinecraftServer getMinecraftServer() {
        return MatthiesenLibApiFabric.getMinecraftServer();
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, ResourceLocation id, Supplier<T> object) {
        final T registeredObject = Registry.register((Registry<T>) registry, id, object.get());
        return () -> registeredObject;
    }
}
