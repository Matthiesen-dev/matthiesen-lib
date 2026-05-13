package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonCommandPlatform;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class FabricCommandPlatformService implements CommonCommandPlatform {
    @Override
    public void registerCommands(Consumer<CommandRegistrar> registrationHandler) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, context) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(dispatcher, registry, context))
        );
    }
}


