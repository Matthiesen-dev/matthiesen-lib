package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommon;
import dev.matthiesen.common.matthiesen_lib.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ExampleModFabric implements ModInitializer {
    public static MinecraftServer MC_SERVER;

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        ExampleModCommon.initialize();
        CommandRegistrationCallback.EVENT.register(ExampleModCommon::registerCommands);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MC_SERVER = server;
            ExampleModCommon.onStartup();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            MC_SERVER = null;
            ExampleModCommon.onShutdown();
        });
    }
}
