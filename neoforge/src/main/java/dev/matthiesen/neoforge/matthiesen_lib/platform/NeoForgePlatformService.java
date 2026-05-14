package dev.matthiesen.neoforge.matthiesen_lib.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.platform.CommonPlatform;
import dev.matthiesen.neoforge.matthiesen_lib.helper.NeoForgeRegistryHelper;
import dev.matthiesen.neoforge.matthiesen_lib.permission.NeoForgePermissionValidator;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

/**
 * NeoForgePlatformService is the implementation of the CommonPlatform interface for the NeoForge mod loader. It provides
 * methods for registering blocks, items, sounds, creative tabs, criteria triggers, stats, menu types, data component types,
 * and entity effects using the NeoForge API. It also includes utilities for checking if a mod is loaded and if the environment
 * is a development environment. This class serves as the bridge between the common code in MatthiesenLib and the specific implementation details of the NeoForge platform.
 */
public class NeoForgePlatformService implements CommonPlatform {
    /**
     * Default constructor for the NeoForgePlatformService. No initialization is required as all methods are stateless and rely on the NeoForge API for registration and utilities.
     */
    public NeoForgePlatformService() {}

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return NeoForgeRegistryHelper.registerBlockEntity(id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return NeoForgeRegistryHelper.registerBlock(id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return NeoForgeRegistryHelper.registerItem(id, item);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return NeoForgeRegistryHelper.registerSound(id, sound);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return NeoForgeRegistryHelper.registerCreativeModeTab(id, tab);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return NeoForgeRegistryHelper.registerCriteriaTriggers(id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return NeoForgeRegistryHelper.registerStats(id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return NeoForgeRegistryHelper.registerMenuType(id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return NeoForgeRegistryHelper.registerDataComponentType(id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return NeoForgeRegistryHelper.registerEntityEffects(name, codec);
    }

    @Override
    public void registerPermissionValidator() {
        MatthiesenLib.setPermissionValidator(new NeoForgePermissionValidator());
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}
