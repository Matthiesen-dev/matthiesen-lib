package dev.matthiesen.common.matthiesen_lib_api;

import com.mojang.serialization.MapCodec;
import dev.faststats.ErrorTracker;
import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.core.*;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.*;
import dev.matthiesen.common.matthiesen_lib_api.core.permission.MatthiesenLibVanillaMatthiesenLibPermissionValidator;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibPlatform;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
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

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Main API class for MatthiesenLib API. This class is responsible for initializing the API and ensuring that it is only
 * initialized once. It provides a static method modInitializer() that can be called by mod loaders to initialize the API,
 * and it uses a private static boolean field to track whether the API has already been initialized to prevent multiple initializations.
 */
@SuppressWarnings("unused")
public class MatthiesenLibApi {
    private static final MatthiesenLibPlatform PLATFORM =
            ServiceLoader.load(MatthiesenLibPlatform.class).findFirst().orElseThrow();

    private static boolean initialized;
    private static MatthiesenLibPermissionValidator permissionValidator;

    /**
     * The ErrorTracker instance used for capturing and anonymizing errors that occur during metrics collection and submission.
     * This tracker is defined in the MatthiesenLibApiMetricsManager class and is used to ensure that any errors encountered during
     * metrics operations are properly handled and anonymized before being reported. By providing a centralized ErrorTracker, the
     * API can maintain consistency in error handling across all metrics-related functionality, allowing for better debugging and
     * analysis of issues that may arise during metrics collection and submission.
     */
    public static final ErrorTracker ERROR_TRACKER = MatthiesenLibApiMetricsManager.ERROR_TRACKER;

    /**
     * Private constructor to prevent instantiation. This class is not meant to be instantiated, as it only contains static
     * methods and fields for managing the API initialization state.
     */
    private MatthiesenLibApi() {}

    /**
     * Initializes the API. This method is idempotent and will only perform initialization once, even if called multiple
     * times. It logs the initialization of the API using the MatthiesenLibApiConstants logging utility.
     */
    public static void modInitializer() {
        if (initialized) {
            return;
        }

        // Initialize the permissions registry
        MatthiesenLibPermissionsManager.modInitializer();
        // Initialize Permissions Validators
        setPermissionValidator(new MatthiesenLibVanillaMatthiesenLibPermissionValidator());
        PLATFORM.registerPermissionValidator();
        // Initialize the command registry
        MatthiesenLibCommandsManager.modInitializer();
        // Initialize the text parser registry
        MatthiesenLibTextParserManager.modInitializer();
        // Initialize the reload manager
        MatthiesenLibReloadManager.modInitializer();

        // Event Managers
        MatthiesenLibApiPlayerEventsManager.modInitializer();
        MatthiesenLibApiServerEventsManager.modInitializer();

        // Metrics
        MatthiesenLibApiMetricsManager.getMetricContext().ready();

        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initialized API");
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
     * Registers a text parser. This method is thread-safe and can be called at any time. If a parser with the same type is already registered, it will be overwritten.
     * @param parser The text parser to register. The parser's type is determined by its getType() method, and it will be initialized before being added to the registry.
     */
    public static void registerTextParser(MatthiesenLibTextParser parser) {
        MatthiesenLibTextParserManager.registerTextParser(parser);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla parser is returned as a fallback.
     * @param type The type of the text parser to retrieve. This should match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(String type) {
        return MatthiesenLibTextParserManager.getTextParser(type);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla
     * parser is returned as a fallback.
     * @param type The type of the text parser to retrieve, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(MatthiesenLibBuiltInTextParsers type) {
        return MatthiesenLibTextParserManager.getTextParser(type);
    }

    /**
     * Registers a reload runnable for a mod. This runnable will be executed when the reload command is triggered.
     * @param modId The ID of the mod registering the reload runnable. This should be a unique identifier for the mod,
     *              typically the mod ID used in the mod's metadata.
     * @param runnable The runnable to execute during a reload. This should contain the logic that the mod wants to
     *                 perform when a reload is triggered, such as reloading configurations or refreshing data.
     */
    public static void registerReloadRunnable(String modId, Runnable runnable) {
        MatthiesenLibReloadManager.registerReloadRunnable(modId, runnable);
    }

    /**
     * Retrieves the map of registered reload runnables. This can be used by the reload command to execute all registered runnables during a reload.
     * @return A map where the key is the mod ID and the value is the runnable to execute during a reload.
     */
    public static Map<String, Runnable> getReloadRunnables() {
        return MatthiesenLibReloadManager.getReloadRunnables();
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the IPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     * @deprecated This method is deprecated in favor of the overload that accepts a MatthiesenLibPlayerEventHandler, which provides default implementations for player event handling methods and allows for more flexible event handling logic.
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings({"unused", "removal"})
    public static void registerPlayerEventHandler(String modId, MatthiesenLibApiPlayerEventsManager.IPlayerEventHandler handler) {
        MatthiesenLibApiPlayerEventsManager.registerPlayerEventHandler(modId, handler);
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     */
    public static void registerPlayerEventHandler(String modId, MatthiesenLibPlayerEventHandler handler) {
        MatthiesenLibApiPlayerEventsManager.registerPlayerEventHandler(modId, handler);
    }

    /**
     * Registers a server event handler for a specific mod. This method allows mods to register their own implementations of the IServerEventHandler interface,
     * enabling them to receive callbacks for server events such as starting, ticking, and stopping. By registering a server event handler, mods can define custom logic to be executed in response to these events, allowing for enhanced functionality and integration with the server lifecycle.
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibServerEventHandler interface that will handle server events for the specified mod. This parameter allows mods to define their own logic for handling server start, tick, and stop events, enabling custom behavior in response to these events.
     */
    public static void registerServerEventHandler(String modId, MatthiesenLibServerEventHandler handler) {
        MatthiesenLibApiServerEventsManager.registerServerEventHandler(modId, handler);
    }

    /**
     * Get the mod container for a mod with the given mod ID. This method can be used to access information about a loaded mod, such as its metadata, resources,
     * or other properties. The implementation of this method may vary depending on the mod loader, but it generally retrieves the mod container from the list
     * of loaded mods based on the specified mod ID.
     * @param modId The mod ID to get the mod container for. This should be the unique identifier of the mod, which is typically defined in the mod's metadata
     *              and used for registration and integration purposes.
     * @return The mod container for the mod with the given mod ID, or null if no such mod is loaded. The mod container provides access to various properties
     * and information about the mod, allowing you to interact with it in a more detailed way if needed.
     */
    public static MatthiesenLibModContainer getModContainer(String modId) {
        return PLATFORM.getModContainer(modId);
    }

    /**
     * Get the configuration file path for the mod. This method can be used to access the configuration file for the mod, allowing you to read and write configuration
     * @param dir The directory where the configuration file is located. This should be a string that specifies the path to the directory, which must be relative to the game's /config/`dir` directory. The method will combine this directory path with the file name provided in the 'file' parameter to construct the full path to the configuration file for the mod. This allows you to organize your mod's configuration files in a specific subdirectory within the main config directory, helping to keep things organized and preventing conflicts with other mods' configuration files.
     * @param file The name of the configuration file. This should be a string that specifies the name of the file, including the file extension (e.g., "config.json"). The method will combine this file name with the directory path provided in the 'dir' parameter to construct the full path to the configuration file, which can then be accessed for reading or writing configuration data for the mod.
     * @return The Path object representing the full path to the configuration file for the mod. This allows you to access the configuration file in a way that is compatible with the underlying file system and the specific mod loader's conventions for storing configuration files. You can use this Path object to read from or write to the configuration file as needed for your mod's functionality.
     */
    public static Path getModConfig(String dir, String file) {
        return PLATFORM.getModConfig(dir, file);
    }

    /**
     * Get the current environment type (e.g., client, server, or dedicated server) using the platform-specific implementation provided by the CommonPlatform service. This method allows you to determine the current environment in which the mod is running, which can be useful for conditionally executing code that should only run on certain sides (e.g., client-only code or server-only code).
     * @return The current environment type, represented as a value from the MatthiesenLibPlatform.ENVIRONMENT enum. This value indicates whether the mod is running in a client environment, a server environment, or a dedicated server environment, allowing you to make informed decisions about which code to execute based on the current environment context.
     */
    public static MatthiesenLibPlatform.ENVIRONMENT getEnvironmentType() {
        return PLATFORM.getEnvironmentType();
    }
    /**
     * Registers a mod with the metrics system by its mod ID. This method retrieves the mod container for the given mod ID using
     * the MatthiesenLibApi, and if found, extracts the mod name and version to store in the REGISTERED_MODS map. The map uses the
     * mod ID as the key and a string containing the mod name and version as the value. If no mod container is found for the given
     * mod ID, or if the mod is already registered, a warning is logged using the API's logger. This method allows mods to be tracked
     * in the metrics system, providing insight into which mods are present in the environment when metrics are collected.
     * @param modId the mod ID of the mod to register with the metrics system. This should be the unique identifier for the mod, as
     *              defined in its metadata. The method will attempt to retrieve the mod container for this ID and, if successful,
     *              will store the mod's name and version in the REGISTERED_MODS map for tracking in the metrics system. If the mod
     *              ID is invalid or if the mod is already registered, a warning will be logged to inform developers of potential
     *              issues with registration.
     */
    public static void registerModToMetrics(String modId) {
        MatthiesenLibApiMetricsManager.registerMod(modId);
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
            MatthiesenLibApiConstants.createInfoLog("Created registry builder for mod ID: " + modId);
        }

        /**
         * Creates a new CreativeModeTab.Builder instance using the platform-specific implementation provided by the CommonPlatform service.
         * @return a new CreativeModeTab.Builder instance
         */
        @SuppressWarnings("unused")
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
}
