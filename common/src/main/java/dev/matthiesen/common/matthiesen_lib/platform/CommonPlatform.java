package dev.matthiesen.common.matthiesen_lib.platform;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * CommonPlatform is an interface that abstracts away the differences between various Minecraft mod loaders (like Fabric and Forge).
 * It provides methods for registering blocks, items, sounds, creative tabs, criteria triggers, stats, and menu types, as well as general
 * utilities like accessing the Minecraft server and checking if a mod is loaded. This allows mod developers to write code that is compatible with
 * multiple mod loaders without having to worry about the specific implementation details of each loader.
 */
@SuppressWarnings("unused")
public interface CommonPlatform {
    // Registry Helpers
    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType);
    <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block);
    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);
    <T extends SoundEvent> Supplier<T> registerSound(String id, Supplier<T> sound);
    <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab);
    <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(String id, Supplier<T> criterionTrigger);
    <T extends ResourceLocation> Supplier<T> registerStats(String id, Supplier<T> stats);
    <T extends MenuType<?>> Supplier<T> registerMenuType(String id, Supplier<T> menuType);

    // Utilities
    MinecraftServer server();
    boolean isModLoaded(String modId);
    boolean isDevelopmentEnvironment();
    CreativeModeTab.Builder newCreativeTabBuilder();
}
