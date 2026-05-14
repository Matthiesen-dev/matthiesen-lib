package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonCommandPlatform;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.function.Consumer;

/**
 * Implementation of the CommonCommandPlatform interface for the Fabric mod loader, utilizing Fabric's CommandRegistrationCallback to register commands.
 */
@SuppressWarnings("unused")
public class FabricCommandPlatformService implements CommonCommandPlatform {
    /**
     * Default constructor for the FabricCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     */
    public FabricCommandPlatformService() {}

    @Override
    public void registerCommands(Consumer<CommandRegistrar> registrationHandler) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, context) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(dispatcher, registry, context))
        );
    }
}


