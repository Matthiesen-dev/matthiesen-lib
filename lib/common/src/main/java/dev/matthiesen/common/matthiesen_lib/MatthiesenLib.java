package dev.matthiesen.common.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCreativeModeTabSectionsManager;
import dev.matthiesen.common.matthiesen_lib.core.network.NetworkingManager;
import dev.matthiesen.common.matthiesen_lib.core.network.PacketContext;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiPlayerEventsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.*;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibExtendedTextParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Main class for the MatthiesenLib mod. This class is responsible for initializing the mod and setting up any necessary
 * configurations or resources. It serves as the entry point for the mod's functionality and can be used to register common
 * features that are shared across different platforms (e.g., Fabric, Forge). The initialize method is called during the
 * mod's initialization phase to perform any necessary setup tasks.
 */
@SuppressWarnings("unused")
public class MatthiesenLib {
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
        MatthiesenLibApi.registerModToApiMetrics(MatthiesenLibConstants.MOD_ID);
        MatthiesenLibCreativeModeTabSectionsManager.init();

        // Final step: Log that the core has been initialized.
        MatthiesenLibConstants.createInfoLog("Initialized Lib");
    }

    /**
     * Provides access to the current PermissionValidator instance used by MatthiesenLib for validating permissions. This
     * allows external code to retrieve the current permission validator and use it for permission checks as needed.
     * @return the current PermissionValidator instance used by MatthiesenLib for validating permissions. This instance
     * is responsible for checking if a given permission is granted to a specific user or context, and it can be used by
     * external code to perform permission checks when necessary.
     */
    public static MatthiesenLibPermissionValidator getPermissionValidator() {
        return MatthiesenLibApi.getPermissionValidator();
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
        MatthiesenLibApi.setPermissionValidator(newValue);
    }

    /**
     * Registers a permission to the permissions' registry.
     * @param permission The permission to register
     */
    public static void registerPermission(Permission permission) {
        MatthiesenLibApi.registerPermission(permission);
    }

    /**
     * Retrieves all registered permissions.
     *
     * @return An unmodifiable list of all permissions.
     */
    public static List<Permission> getAllRegisteredPermissions() {
        return MatthiesenLibApi.getAllRegisteredPermissions();
    }

    /**
     * Gets the count of registered pending permissions.
     *
     * @return The number of permissions pending registration.
     */
    public static int getPendingPermissionCount() {
        return MatthiesenLibApi.getPendingPermissionCount();
    }

    /**
     * Registers a command implementation using the platform-agnostic command registry.
     * @param command The command to register
     */
    public static void registerCommand(AbstractCommand command) {
        MatthiesenLibApi.registerCommand(command);
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MatthiesenLibApi.getMinecraftServer();
    }

    /**
     * Checks if a mod with the given mod ID is loaded using the platform-specific implementation provided by the CommonPlatform service.
     * @param modId The mod ID to check for (e.g., "minecraft", "fabric", "forge")
     * @return true if the mod is loaded, false otherwise
     */
    public static boolean isModLoaded(String modId) {
        return MatthiesenLibApi.isModLoaded(modId);
    }

    /**
     * Checks if the current environment is a development environment using the platform-specific implementation provided by the CommonPlatform service.
     * @return true if the current environment is a development environment, false otherwise
     */
    public static boolean isDevelopmentEnvironment() {
        return MatthiesenLibApi.isDevelopmentEnvironment();
    }


    /**
     * Registers a text parser. This method is thread-safe and can be called at any time. If a parser with the same type is already registered, it will be overwritten.
     * @param parser The text parser to register. The parser's type is determined by its getType() method, and it will be initialized before being added to the registry.
     */
    public static void registerTextParser(MatthiesenLibTextParser parser) {
        MatthiesenLibApi.registerTextParser(parser);
    }

    /**
     * Registers an extended text parser. This method is thread-safe and can be called at any time. If a parser with the same type is already registered, it will be overwritten.
     * @param parser The extended text parser to register. The parser's type is determined by its getType() method, and it will be initialized before being added to the registry. Extended text parsers provide additional functionality, such as compatibility bridges for specific mods (e.g., Embers), while still conforming to the basic text parser contract.
     */
    public static void registerTextParser(MatthiesenLibExtendedTextParser parser) {
        MatthiesenLibApi.registerTextParser(parser);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla parser is returned as a fallback.
     * @param type The type of the text parser to retrieve. This should match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(String type) {
        return MatthiesenLibApi.getTextParser(type);
    }

    /**
     * Retrieves a registered text parser by its type. If no parser is registered for the given type, a warning is logged and the vanilla
     * parser is returned as a fallback.
     * @param type The type of the text parser to retrieve, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return The text parser registered for the given type, or the vanilla parser if no such parser is registered.
     */
    public static MatthiesenLibTextParser getTextParser(MatthiesenLibBuiltInTextParsers type) {
        return MatthiesenLibApi.getTextParser(type);
    }

    /**
     * Retrieves the Embers compatibility bridge from the currently-registered Embers text parser, if available.
     *
     * <p>This keeps Embers access discoverable from the main MatthiesenLib facade while the core parser contract
     * itself remains server-safe in the API module.</p>
     *
     * @return An Embers compatibility implementation when an Embers-capable parser is registered, or {@code null} otherwise.
     */
    public static @Nullable MatthiesenLibEmbersTextParserCompat getEmbersTextParserCompat() {
        return getEmbersTextParserCompat(MatthiesenLibBuiltInTextParsers.EMBER.getName());
    }

    /**
     * Retrieves the Embers compatibility bridge from the parser registered under the given built-in parser type, if available.
     *
     * @param type The built-in parser type to inspect.
     * @return An Embers compatibility implementation when available, or {@code null} otherwise.
     */
    public static @Nullable MatthiesenLibEmbersTextParserCompat getEmbersTextParserCompat(MatthiesenLibBuiltInTextParsers type) {
        return getEmbersTextParserCompat(type.getName());
    }

    /**
     * Retrieves the Embers compatibility bridge from the parser registered under the given type, if available.
     *
     * @param type The parser type to inspect.
     * @return An Embers compatibility implementation when available, or {@code null} otherwise.
     */
    public static @Nullable MatthiesenLibEmbersTextParserCompat getEmbersTextParserCompat(String type) {
        MatthiesenLibTextParser parser = MatthiesenLibTextParserManager.getTextParser(type);
        if (parser instanceof MatthiesenLibExtendedTextParser commonParser) {
            return commonParser.getEmbersCompat();
        }
        return null;
    }

    /**
     * Checks if a text parser is registered for the given type.
     * @param type The type of the text parser to check for. This should match the value returned by the getType() method of the desired parser.
     * @return {@code true} if a text parser is registered for the given type, {@code false} otherwise.
     */
    public static boolean isTextParserInitialized(String type) {
        return MatthiesenLibTextParserManager.isTextParserInitialized(type);
    }

    /**
     * Checks if a text parser is registered for the given type.
     * @param type The type of the text parser to check for, represented as a MatthiesenLibBuiltInTextParsers enum value. This should
     *             match the value returned by the getType() method of the desired parser.
     * @return {@code true} if a text parser is registered for the given type, {@code false} otherwise.
     */
    public static boolean isTextParserInitialized(MatthiesenLibBuiltInTextParsers type) {
        return MatthiesenLibTextParserManager.isTextParserInitialized(type);
    }

    /**
     * Registers a reload runnable for a mod. This runnable will be executed when the reload command is triggered.
     * @param modId The ID of the mod registering the reload runnable. This should be a unique identifier for the mod,
     *              typically the mod ID used in the mod's metadata.
     * @param runnable The runnable to execute during a reload. This should contain the logic that the mod wants to
     *                 perform when a reload is triggered, such as reloading configurations or refreshing data.
     */
    public static void registerReloadRunnable(String modId, Runnable runnable) {
        MatthiesenLibApi.registerReloadRunnable(modId, runnable);
    }

    /**
     * Retrieves the map of registered reload runnables. This can be used by the reload command to execute all registered runnables during a reload.
     * @return A map where the key is the mod ID and the value is the runnable to execute during a reload.
     */
    public static Map<String, Runnable> getReloadRunnables() {
        return MatthiesenLibApi.getReloadRunnables();
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the IPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     * @deprecated This method is deprecated in favor of the overload that accepts a MatthiesenLibPlayerEventHandler, which provides a more specific contract for handling player events. The original method accepted a more generic IPlayerEventHandler, which may have led to confusion or misuse. The new method ensures that only valid player event handlers can be registered, improving type safety and clarity in the API.
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings({"unused", "removal"})
    public static void registerPlayerEventHandler(String modId, MatthiesenLibApiPlayerEventsManager.IPlayerEventHandler handler) {
        MatthiesenLibApi.registerPlayerEventHandler(modId, handler);
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     */
    public static void registerPlayerEventHandler(String modId, MatthiesenLibPlayerEventHandler handler) {
        MatthiesenLibApi.registerPlayerEventHandler(modId, handler);
    }

    /**
     * Registers a server event handler for a specific mod. This method allows mods to register their own implementations of the IServerEventHandler interface,
     * enabling them to receive callbacks for server events such as starting, ticking, and stopping. By registering a server event handler, mods can define custom logic to be executed in response to these events, allowing for enhanced functionality and integration with the server lifecycle.
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibServerEventHandler interface that will handle server events for the specified mod. This parameter allows mods to define their own logic for handling server start, tick, and stop events, enabling custom behavior in response to these events.
     */
    public static void registerServerEventHandler(String modId, MatthiesenLibServerEventHandler handler) {
        MatthiesenLibApi.registerServerEventHandler(modId, handler);
    }

    /**
     * Retrieves the networking utilities instance for handling network-related operations.
     * <pre>
     * // Common Module Packet Setup Example:
     * public class ModPackets {
     *     public static void init() {
     *         MatthiesenLib.networkingUtils.registerC2S(
     *             ValueUpdatePacket.TYPE,
     *             ValueUpdatePacket.STREAM_CODEC,
     *             (packet, context) -> context.enqueue(() -> {
     *                 var player = context.player();
     *                 var stack = player.getItemInHand(InteractionHand.MAIN_HAND);
     *                 if (stack.is(MyItems.CUSTOM_ITEM.get())) {
     *                     stack.set(MyComponents.MY_VALUE.get(), packet.value());
     *                 }
     *             })
     *         );
     *         MatthiesenLib.networkingUtils.registerS2C(
     *             ValueUpdatePacket.TYPE,
     *             ValueUpdatePacket.STREAM_CODEC,
     *             (packet, context) -> context.enqueue(() -> {
     *                 // RUNNING ON CLIENT THREAD SAFELY
     *                 var clientPlayer = context.player();
     *                 // Update client graphics, HUD overlays, or screen instances here!
     *             })
     *         );
     *     }
     * }
     *
     * // Then use it in your Screen:
     * MatthiesenLib.networkingUtils.sendToServer(new ValueUpdatePacket(newValue));
     * MatthiesenLib.networkingUtils.sendToPlayer(serverPlayerInstance, new ValueSyncPacket(synchronizedValue));
     * </pre>
     */
    public static final NetworkingUtils networkingUtils = new NetworkingUtils();

    /**
     * Utilities for interacting and registering with Platform Networking
     */
    public static class NetworkingUtils {
        /**
         * Registers a client-to-server (C2S) packet type with the specified codec and handler.
         * @param type The custom packet type to register.
         * @param codec The codec for encoding and decoding the packet.
         * @param handler The handler to process the packet when received.
         * @param <T> The type of the custom packet payload.
         */
        public <T extends CustomPacketPayload> void registerC2S(
                CustomPacketPayload.Type<T> type,
                StreamCodec<RegistryFriendlyByteBuf, T> codec,
                BiConsumer<T, PacketContext> handler
        ) {
            NetworkingManager.registerC2S(type, codec, handler);
        }

        /**
         * Registers a server-to-client (S2C) packet type with the specified codec and handler.
         * @param type The custom packet type to register.
         * @param codec The codec for encoding and decoding the packet.
         * @param handler The handler to process the packet when received.
         * @param <T> The type of the custom packet payload.
         */
        public <T extends CustomPacketPayload> void registerS2C(
                CustomPacketPayload.Type<T> type,
                StreamCodec<RegistryFriendlyByteBuf, T> codec,
                BiConsumer<T, PacketContext> handler
        ) {
            NetworkingManager.registerS2C(type, codec, handler);
        }

        /**
         * Sends a custom packet payload to the server.
         * @param payload The custom packet payload to send.
         */
        public void sendToServer(CustomPacketPayload payload) {
            NetworkingManager.sendToServer(payload);
        }

        /**
         * Sends a custom packet payload to a specific player on the server.
         * @param player The player to send the packet to.
         * @param payload The custom packet payload to send.
         */
        public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
            NetworkingManager.sendToPlayer(player, payload);
        }
    }

    /**
     * A builder class for registering various types of content (e.g., items, blocks, block entities, etc.) with automatic prefixing of the mod ID to the ResourceLocation IDs.
     */
    public static class RegistryBuilder extends MatthiesenLibApi.RegistryBuilder {
        /**
         * Creates a new RegistryBuilder instance for the specified mod ID. This builder provides convenient methods for registering various types of
         * content (e.g., items, blocks, block entities, etc.) with automatic prefixing of the mod ID to the ResourceLocation IDs. This helps to ensure that
         * all registered content is properly namespaced and avoids potential conflicts with other mods.
         *
         * @param modId The mod ID to use as a prefix for all registered content. This should be the unique identifier for your mod (e.g., "mymod"),
         *              and it will be automatically prefixed to the names of all registered items, blocks, etc. when creating their ResourceLocation IDs.
         */
        public RegistryBuilder(String modId) {
            super(modId);
        }
    }
}
