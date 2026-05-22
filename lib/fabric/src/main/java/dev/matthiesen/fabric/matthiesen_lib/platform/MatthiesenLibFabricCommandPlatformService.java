package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.api.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibCommandRegistrar;
import dev.matthiesen.api.matthiesen_lib.core.platform.MatthiesenLibCommandPlatform;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.function.Consumer;

/**
 * Implementation of the MatthiesenLibCommandPlatform interface for the Fabric mod loader, utilizing Fabric's CommandRegistrationCallback to register commands.
 */
@SuppressWarnings("unused")
public class MatthiesenLibFabricCommandPlatformService implements MatthiesenLibCommandPlatform {
    /**
     * Default constructor for the MatthiesenLibFabricCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     */
    public MatthiesenLibFabricCommandPlatformService() {}

    @Override
    public void registerCommands(Consumer<MatthiesenLibCommandRegistrar> registrationHandler) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, context) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(dispatcher, registry, context))
        );
    }
}


