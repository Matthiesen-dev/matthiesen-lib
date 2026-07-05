package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiPlayerEventsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiServerEventsManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import java.util.Map;

/**
 * Main class for the MatthiesenLib API on the Fabric platform. This class is responsible for initializing the API and
 * managing the Minecraft server instance.
 * It provides a thread-safe way to access the current Minecraft server instance through the getMinecraftServer method.
 * The server instance is updated in response to server lifecycle events, ensuring that the API has access to the server
 * instance when it is running and prevents access when the server is not running.
 */
public class MatthiesenLibApiFabric implements ModInitializer {
    private static volatile MinecraftServer MC_SERVER;

    /**
     * Default constructor for the MatthiesenLibApiFabric class. This constructor is required by the Fabric mod loader to instantiate the mod class when loading the mod.
     */
    public MatthiesenLibApiFabric() {}

    /**
     * Default constructor for the MatthiesenLibApiFabric class. Initializes the API and registers necessary resources.
     * This method is called when the mod is loaded by the Fabric mod loader.
     */
    @Override
    public void onInitialize() {
        MatthiesenLibApiConstants.createInfoLog("Loading API for Fabric Mod Loader");
        MatthiesenLibApi.modInitializer();
        // Register Server events
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MC_SERVER = server;
            MatthiesenLibApiServerEventsManager.onServerStart(server);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            MatthiesenLibApiServerEventsManager.onServerStop(server);
            MC_SERVER = null;
        });
        ServerTickEvents.END_SERVER_TICK.register(MatthiesenLibApiServerEventsManager::onServerTick);

        // Register Server Reload Event
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                Map<String, Runnable> runnables = MatthiesenLibApi.getReloadRunnables();
                if (runnables.isEmpty()) return;
                for (Map.Entry<String, Runnable> entry : runnables.entrySet()) {
                    try {
                        MatthiesenLibApiConstants.createExtendedLog("Executing reload runnable for mod: " + entry.getKey());
                        entry.getValue().run();
                    } catch (Exception e) {
                        MatthiesenLibApiMetricsManager.ERROR_TRACKER.trackError(e);
                        MatthiesenLibApiConstants.createErrorLog("Error executing reload runnable for mod: " + entry.getKey(), e);
                    }
                }
            }
        });

        // Player Events
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) ->
                MatthiesenLibApiPlayerEventsManager.onPlayerJoin(handler.getPlayer())));
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) ->
                MatthiesenLibApiPlayerEventsManager.onPlayerLeave(handler.getPlayer())));

        UseItemCallback.EVENT.register((player, level, interactionHand) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                MatthiesenLibApiPlayerEventsManager.onPlayerUseItem(serverPlayer, level, interactionHand);
            }

            return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(interactionHand));
        });
        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                MatthiesenLibApiPlayerEventsManager.onPlayerUseBlock(serverPlayer, level, interactionHand, blockHitResult.getBlockPos());
            }

            return InteractionResult.PASS;
        });
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }
}
