package dev.matthiesen.common.matthiesen_lib;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCommandsManager;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibPermissionsManager;
import dev.matthiesen.common.matthiesen_lib.core.permission.MatthiesenLibVanillaMatthiesenLibPermissionValidator;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibPermissionValidator;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibPlatform;
import dev.matthiesen.common.matthiesen_lib.permission.Permission;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
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

import java.util.List;
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
    private static final MatthiesenLibPlatform PLATFORM =
            ServiceLoader.load(MatthiesenLibPlatform.class).findFirst().orElseThrow();
    private static MatthiesenLibPermissionValidator permissionValidator;
    private static boolean initialized;

    /**
     * Default constructor for the MatthiesenLib class. This constructor does not perform any initialization, as the mod's setup is handled in the modInitializer method.
     * The constructor is provided for completeness and to allow for potential future use if instance-specific initialization is needed, but currently, all functionality is
     * static and does not require an instance of the MatthiesenLib class.
     */
    private MatthiesenLib() {}

    /**
     * Initializes the MatthiesenLib mod. (Do not run this from an external mod. This is used to set up the MatthiesenLib Mod)
     */
    public static void modInitializer() {
        if (initialized) {
            return;
        }

        initialized = true;
        // Initialize the permissions registry
        MatthiesenLibPermissionsManager.modInitializer();

        // Pre-register the Vanilla MC permissions validator
        setPermissionValidator(new MatthiesenLibVanillaMatthiesenLibPermissionValidator());

        // Register any platform permission validator available through the CommonPlatform service.
        PLATFORM.registerPermissionValidator();
        MatthiesenLibCommandsManager.modInitializer();
        MatthiesenLibConstants.createInfoLog("Initialized core");
    }

    /**
     * Provides access to the current PermissionValidator instance used by MatthiesenLib for validating permissions. This
     * allows external code to retrieve the current permission validator and use it for permission checks as needed.
     * @return the current PermissionValidator instance used by MatthiesenLib for validating permissions. This instance
     * is responsible for checking if a given permission is granted to a specific user or context, and it can be used by
     * external code to perform permission checks when necessary.
     */
    public static MatthiesenLibPermissionValidator getPermissionValidator() {
        return permissionValidator;
    }

    /**
     * Sets the PermissionValidator instance to be used by MatthiesenLib for validating permissions. This allows for
     * flexibility in choosing different permission validation implementations, such as a vanilla Minecraft-based validator
     * or a custom implementation provided by a specific platform (e.g., Fabric, Forge).
     * @param newValue the new PermissionValidator instance to use for validating permissions. This should be an instance
     *                 of a class that implements the PermissionValidator interface, and it will be used by MatthiesenLib
     *                 to validate permissions when needed.
     */
    public static void setPermissionValidator(MatthiesenLibPermissionValidator newValue) {
        permissionValidator = newValue;
        newValue.initialize();
    }

    /**
     * Registers a permission to the permissions' registry.
     * @param permission The permission to register
     */
    public static void registerPermission(Permission permission) {
        MatthiesenLibPermissionsManager.registerPermission(permission);
    }

    /**
     * Retrieves all registered permissions.
     *
     * @return An unmodifiable list of all permissions.
     */
    public static List<Permission> getAllRegisteredPermissions() {
        return MatthiesenLibPermissionsManager.all();
    }

    /**
     * Gets the count of registered pending permissions.
     *
     * @return The number of permissions pending registration.
     */
    public static int getPendingPermissionCount() {
        return MatthiesenLibPermissionsManager.getPendingPermissionCount();
    }

    /**
     * Registers a command implementation using the platform-agnostic command registry.
     * @param command The command to register
     */
    public static void registerCommand(AbstractCommand command) {
        MatthiesenLibCommandsManager.registerCommand(command);
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return PLATFORM.getMinecraftServer();
    }

    /**
     * Checks if a mod with the given mod ID is loaded using the platform-specific implementation provided by the CommonPlatform service.
     * @param modId The mod ID to check for (e.g., "minecraft", "fabric", "forge")
     * @return true if the mod is loaded, false otherwise
     */
    public static boolean isModLoaded(String modId) {
        return PLATFORM.isModLoaded(modId);
    }

    /**
     * Checks if the current environment is a development environment using the platform-specific implementation provided by the CommonPlatform service.
     * @return true if the current environment is a development environment, false otherwise
     */
    public static boolean isDevelopmentEnvironment() {
        return PLATFORM.isDevelopmentEnvironment();
    }

    /**
     * A builder class for registering various types of content (e.g., items, blocks, block entities, etc.) with automatic prefixing of the mod ID to the ResourceLocation IDs.
     */
    public static class RegistryBuilder {
        /**
         * The mod ID to use as a prefix for all registered content. This should be the unique identifier for your mod (e.g., "mymod"),
         * and it will be automatically prefixed to the names of all registered items, blocks, etc. when creating their ResourceLocation IDs.
         * This helps to ensure that all registered content is properly namespaced and avoids potential conflicts with other mods.
         */
        private final String modId;

        /**
         * Creates a new RegistryBuilder instance for the specified mod ID. This builder provides convenient methods for registering various types of
         * content (e.g., items, blocks, block entities, etc.) with automatic prefixing of the mod ID to the ResourceLocation IDs. This helps to ensure that
         * all registered content is properly namespaced and avoids potential conflicts with other mods.
         * @param modId The mod ID to use as a prefix for all registered content. This should be the unique identifier for your mod (e.g., "mymod"),
         *              and it will be automatically prefixed to the names of all registered items, blocks, etc. when creating their ResourceLocation IDs.
         */
        public RegistryBuilder(String modId) {
            this.modId = modId;
            MatthiesenLibConstants.createInfoLog("Created registry builder for mod ID: " + modId);
        }

        /**
         * Creates a new CreativeModeTab.Builder instance using the platform-specific implementation provided by the CommonPlatform service.
         * @return a new CreativeModeTab.Builder instance
         */
        public CreativeModeTab.Builder newCreativeTabBuilder() {
            return PLATFORM.newCreativeTabBuilder();
        }

        /**
         * Registers a new item with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the item being registered. This should be a subclass of Item, and it will be used to create instances of the item when needed.
         * @param name The name to register the item under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param item A Supplier that provides an instance of the Item to register. This supplier will be called when the item needs
         *             to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered Item. This allows other parts of the mod to access the item after it has
         * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
            return PLATFORM.registerItem(ResourceLocation.fromNamespaceAndPath(modId, name), item);
        }

        /**
         * Registers a new block with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the block being registered. This should be a subclass of Block, and it will be used to create instances of the block when needed.
         * @param name The name to register the block under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param block A Supplier that provides an instance of the Block to register. This supplier will be called when the block
         *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered Block. This allows other parts of the mod to access the block after it has
         * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
            return PLATFORM.registerBlock(ResourceLocation.fromNamespaceAndPath(modId, name), block);
        }

        /**
         * Registers a new block entity type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the block entity being registered. This should be a subclass of BlockEntity, and it will be
         *           used to create instances of the block entity when needed.
         * @param name The name to register the block entity type under. This should be unique within the mod and should follow the standard
         *             format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param blockEntitySupplier A Supplier that provides an instance of the BlockEntityType to register. This supplier will be called when the block entity type needs to be created,
         *                            allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered BlockEntityType. This allows other parts of the mod to access the block entity type after it has been registered,
         *                        and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> blockEntitySupplier) {
            return PLATFORM.registerBlockEntity(ResourceLocation.fromNamespaceAndPath(modId, name), blockEntitySupplier);
        }

        /**
         * Registers a new creative mode tab with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the creative mode tab being registered. This should be a subclass of CreativeModeTab, and it will be
         *           used to create instances of the creative mode tab when needed.
         * @param name The name to register the creative mode tab under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param tab A Supplier that provides an instance of the CreativeModeTab to register. This supplier will be called when the
         *            creative mode tab needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered CreativeModeTab. This allows other parts of the mod to access the creative
         * mode tab after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String name, Supplier<T> tab) {
            return PLATFORM.registerCreativeModeTab(ResourceLocation.fromNamespaceAndPath(modId, name), tab);
        }

        /**
         * Registers a new sound event with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the sound event being registered. This should be a subclass of SoundEvent, and it will be used to create instances of the sound event when needed.
         * @param name The name to register the sound event under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param sound A Supplier that provides an instance of the SoundEvent to register. This supplier will be called when the sound
         *              event needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered SoundEvent. This allows other parts of the mod to access the sound event after
         * it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
            return PLATFORM.registerSound(ResourceLocation.fromNamespaceAndPath(modId, name), sound);
        }

        /**
         * Registers a new custom criterion trigger with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the criterion trigger being registered. This should be a subclass of CriterionTrigger, and it will be
         *           used to create instances of the criterion trigger when needed.
         * @param name The name to register the criterion trigger under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param criterionTrigger A Supplier that provides an instance of the CriterionTrigger to register. This supplier will be called
         *                         when the criterion trigger needs to be created, allowing for lazy initialization and avoiding potential
         *                         issues with static initialization order.
         * @return A Supplier that provides the registered CriterionTrigger. This allows other parts of the mod to access the criterion
         * trigger after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(String name, Supplier<T> criterionTrigger) {
            return PLATFORM.registerCriteriaTriggers(ResourceLocation.fromNamespaceAndPath(modId, name), criterionTrigger);
        }

        /**
         * Registers a new custom statistic with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the statistic being registered. This should be a subclass of ResourceLocation, and it will be used to
         *           create instances of the statistic when needed.
         * @param name The name to register the statistic under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param stats A Supplier that provides an instance of the statistic to register. This supplier will be called when the statistic
         *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered statistic. This allows other parts of the mod to access the statistic after it
         * has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends ResourceLocation> Supplier<T> registerStats(String name, Supplier<T> stats) {
            return PLATFORM.registerStats(ResourceLocation.fromNamespaceAndPath(modId, name), stats);
        }

        /**
         * Registers a new MenuType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the menu being registered. This should be a subclass of MenuType, and it will be used to create instances of the menu when needed.
         * @param name The name to register the menu under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param menuType A Supplier that provides an instance of the MenuType to register. This supplier will be called when the menu type
         *                 needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered MenuType. This allows other parts of the mod to access the menu type after it has
         * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends MenuType<?>> Supplier<T> registerMenuType(String name, Supplier<T> menuType) {
            return PLATFORM.registerMenuType(ResourceLocation.fromNamespaceAndPath(modId, name), menuType);
        }

        /**
         * Registers a new DataComponentType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the data component being registered. This should be a subclass of DataComponentType, and it will be used to create instances of the data component when needed.
         * @param name The name to register the data component type under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param component A Supplier that provides an instance of the DataComponentType to register. This supplier will be called when the
         *                  data component type needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
         * @return A Supplier that provides the registered DataComponentType. This allows other parts of the mod to access the data component
         * type after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
         */
        public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(String name, Supplier<T> component) {
            return PLATFORM.registerDataComponentType(ResourceLocation.fromNamespaceAndPath(modId, name), component);
        }

        /**
         * Registers a new EnchantmentEntityEffect type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
         *
         * @param <T> The type of the enchantment entity effect being registered. This should be a subclass of MapCodec that produces
         *           instances of EnchantmentEntityEffect, and it will be used to create instances of the enchantment entity effect when needed.
         * @param name The name to register the enchantment entity effect under. This should be unique within the mod and should follow the standard
         *             format of "name" (without the mod ID, as it will be prefixed automatically).
         * @param codec A Supplier that provides an instance of the MapCodec to register for the enchantment entity effect. This supplier
         *              will be called when the enchantment entity effect needs to be created, allowing for lazy initialization and avoiding
         *              potential issues with static initialization order.
         * @return A Supplier that provides the registered MapCodec for the enchantment entity effect. This allows other parts of the mod to
         * access the enchantment entity effect codec after it has been registered, and it will return the correct instance regardless of when
         * it is called during the mod's initialization process.
         */
        public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(String name, Supplier<T> codec) {
            return PLATFORM.registerEntityEffects(ResourceLocation.fromNamespaceAndPath(modId, name), codec);
        }
    }

    // TODO: DEPRECATED - TO BE REMOVED

    /**
     * Creates a new CreativeModeTab.Builder instance using the platform-specific implementation provided by the CommonPlatform service.
     * @return a new CreativeModeTab.Builder instance
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static CreativeModeTab.Builder newCreativeTabBuilder() {
        return PLATFORM.newCreativeTabBuilder();
    }

    /**
     * Registers a new block entity type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the block entity being registered. This should be a subclass of BlockEntity, and it will be
     *           used to create instances of the block entity when needed.
     * @param id The ResourceLocation ID to register the block entity type under. This should be unique within the mod and
     *           should follow the standard format of "modid:name".
     * @param blockEntityType A Supplier that provides an instance of the BlockEntityType to register. This supplier will
     *                        be called when the block entity type needs to be created,
     *                        allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered BlockEntityType. This allows other parts of the mod to access the block entity type after it has been registered,
     *                        and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return PLATFORM.registerBlockEntity(id, blockEntityType);
    }

    /**
     * Registers a new block with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the block being registered. This should be a subclass of Block, and it will be used to create instances of the block when needed.
     * @param id The ResourceLocation ID to register the block under. This should be unique within the mod and should follow the standard format of "modid:name".
     * @param block A Supplier that provides an instance of the Block to register. This supplier will be called when the block
     *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered Block. This allows other parts of the mod to access the block after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return PLATFORM.registerBlock(id, block);
    }

    /**
     * Registers a new item with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the item being registered. This should be a subclass of Item, and it will be used to create instances of the item when needed.
     * @param id The ResourceLocation ID to register the item under. This should be unique within the mod and should follow the standard format of "modid:name".
     * @param item A Supplier that provides an instance of the Item to register. This supplier will be called when the item needs
     *             to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered Item. This allows other parts of the mod to access the item after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return PLATFORM.registerItem(id, item);
    }

    /**
     * Registers a new sound event with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the sound event being registered. This should be a subclass of SoundEvent, and it will be used to create instances of the sound event when needed.
     * @param id The ResourceLocation ID to register the sound event under. This should be unique within the mod and should follow the standard format of "modid:name".
     * @param sound A Supplier that provides an instance of the SoundEvent to register. This supplier will be called when the sound
     *              event needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered SoundEvent. This allows other parts of the mod to access the sound event after
     * it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return PLATFORM.registerSound(id, sound);
    }

    /**
     * Registers a new creative mode tab with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the creative mode tab being registered. This should be a subclass of CreativeModeTab, and it will be
     *           used to create instances of the creative mode tab when needed.
     * @param id The ResourceLocation ID to register the creative mode tab under. This should be unique within the mod and should
     *           follow the standard format of "modid:name".
     * @param tab A Supplier that provides an instance of the CreativeModeTab to register. This supplier will be called when the
     *            creative mode tab needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered CreativeModeTab. This allows other parts of the mod to access the creative
     * mode tab after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return PLATFORM.registerCreativeModeTab(id, tab);
    }

    /**
     * Registers a new custom criterion trigger with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the criterion trigger being registered. This should be a subclass of CriterionTrigger, and it will be
     *           used to create instances of the criterion trigger when needed.
     * @param id The ResourceLocation ID to register the criterion trigger under. This should be unique within the mod and should
     *           follow the standard format of "modid:name".
     * @param criterionTrigger A Supplier that provides an instance of the CriterionTrigger to register. This supplier will be called
     *                         when the criterion trigger needs to be created, allowing for lazy initialization and avoiding potential
     *                         issues with static initialization order.
     * @return A Supplier that provides the registered CriterionTrigger. This allows other parts of the mod to access the criterion
     * trigger after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return PLATFORM.registerCriteriaTriggers(id, criterionTrigger);
    }

    /**
     * Registers a new custom statistic with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the statistic being registered. This should be a subclass of ResourceLocation, and it will be used to
     *           create instances of the statistic when needed.
     * @param id The ResourceLocation ID to register the statistic under. This should be unique within the mod and should follow the
     *           standard format of "modid:name".
     * @param stats A Supplier that provides an instance of the statistic to register. This supplier will be called when the statistic
     *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered statistic. This allows other parts of the mod to access the statistic after it
     * has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return PLATFORM.registerStats(id, stats);
    }

    /**
     * Registers a new MenuType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the menu being registered. This should be a subclass of MenuType, and it will be used to create instances of the menu when needed.
     * @param id The ResourceLocation ID to register the menu type under. This should be unique within the mod and should follow the standard format of "modid:name".
     * @param menuType A Supplier that provides an instance of the MenuType to register. This supplier will be called when the menu type
     *                 needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered MenuType. This allows other parts of the mod to access the menu type after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return PLATFORM.registerMenuType(id, menuType);
    }

    /**
     * Registers a new DataComponentType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the data component being registered. This should be a subclass of DataComponentType, and it will be used to create instances of the data component when needed.
     * @param id The ResourceLocation ID to register the data component type under. This should be unique within the mod and should follow the standard format of "modid:name".
     * @param component A Supplier that provides an instance of the DataComponentType to register. This supplier will be called when the
     *                  data component type needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered DataComponentType. This allows other parts of the mod to access the data component
     * type after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return PLATFORM.registerDataComponentType(id, component);
    }

    /**
     * Registers a new EnchantmentEntityEffect type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the enchantment entity effect being registered. This should be a subclass of MapCodec that produces
     *           instances of EnchantmentEntityEffect, and it will be used to create instances of the enchantment entity effect when needed.
     * @param id The ResourceLocation ID to register the enchantment entity effect under. This should be unique within the mod and
     *           should follow the standard format of "modid:name".
     * @param codec A Supplier that provides an instance of the MapCodec to register for the enchantment entity effect. This supplier
     *              will be called when the enchantment entity effect needs to be created, allowing for lazy initialization and avoiding
     *              potential issues with static initialization order.
     * @return A Supplier that provides the registered MapCodec for the enchantment entity effect. This allows other parts of the mod to
     * access the enchantment entity effect codec after it has been registered, and it will return the correct instance regardless of when
     * it is called during the mod's initialization process.
     *
     * @deprecated Use the new RegistryBuilder API instead, which allows for automatic prefixing of the mod ID and better organization of registrations.
     * This method will still work, but it is recommended to switch to the RegistryBuilder API for new code and to eventually migrate existing code to it
     * for consistency and improved usability.
     */
    @Deprecated(forRemoval = true)
    public static <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation id, Supplier<T> codec) {
        return PLATFORM.registerEntityEffects(id, codec);
    }
}
