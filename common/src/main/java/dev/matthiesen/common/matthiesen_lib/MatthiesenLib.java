package dev.matthiesen.common.matthiesen_lib;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.command.MatthiesenLibCommands;
import dev.matthiesen.common.matthiesen_lib.platform.CommonPlatform;
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

import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Main class for the MatthiesenLib mod. This class is responsible for initializing the mod and setting up any necessary
 * configurations or resources. It serves as the entry point for the mod's functionality and can be used to register common
 * features that are shared across different platforms (e.g., Fabric, Forge). The initialize method is called during the
 * mod's initialization phase to perform any necessary setup tasks.
 */
@SuppressWarnings("unused")
public class MatthiesenLib {
    private static final CommonPlatform COMMON_PLATFORM =
            ServiceLoader.load(CommonPlatform.class).findFirst().orElseThrow();

    private static boolean initialized;

    /**
     * Initializes the MatthiesenLib mod. (Do not run this from an external mod. This is used to set up the MatthiesenLib Mod)
     */
    public static void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        MatthiesenLibCommands.modInitializer();
        Constants.createInfoLog("Initialized common");
    }

    /**
     * Registers a command implementation using the platform-agnostic command registry.
     */
    public static void registerCommand(AbstractCommand command) {
        MatthiesenLibCommands.registerCommand(command);
    }

    /**
     * Checks if a mod with the given mod ID is loaded using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static boolean isModLoaded(String modId) {
        return COMMON_PLATFORM.isModLoaded(modId);
    }

    /**
     * Checks if the current environment is a development environment using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static boolean isDevelopmentEnvironment() {
        return COMMON_PLATFORM.isDevelopmentEnvironment();
    }

    /**
     * Creates a new CreativeModeTab.Builder instance using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static CreativeModeTab.Builder newCreativeTabBuilder() {
        return COMMON_PLATFORM.newCreativeTabBuilder();
    }

    /**
     * Registers a new block entity type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return COMMON_PLATFORM.registerBlockEntity(id, blockEntityType);
    }

    /**
     * Registers a new block with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return COMMON_PLATFORM.registerBlock(id, block);
    }

    /**
     * Registers a new item with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return COMMON_PLATFORM.registerItem(id, item);
    }

    /**
     * Registers a new sound event with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return COMMON_PLATFORM.registerSound(id, sound);
    }

    /**
     * Registers a new creative mode tab with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return COMMON_PLATFORM.registerCreativeModeTab(id, tab);
    }

    /**
     * Registers a new custom criterion trigger with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return COMMON_PLATFORM.registerCriteriaTriggers(id, criterionTrigger);
    }

    /**
     * Registers a new custom statistic with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return COMMON_PLATFORM.registerStats(id, stats);
    }

    /**
     * Registers a new MenuType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return COMMON_PLATFORM.registerMenuType(id, menuType);
    }

    /**
     * Registers a new DataComponentType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return COMMON_PLATFORM.registerDataComponentType(id, component);
    }

    /**
     * Registers a new EnchantmentEntityEffect type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     */
    public static <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation id, Supplier<T> codec) {
        return COMMON_PLATFORM.registerEntityEffects(id, codec);
    }
}
