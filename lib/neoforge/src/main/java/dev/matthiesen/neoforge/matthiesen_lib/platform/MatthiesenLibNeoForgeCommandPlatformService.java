package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibCommandRegistrar;
import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibCommandPlatform;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Consumer;

/**
 * Implementation of the MatthiesenLibCommandPlatform interface for the NeoForge mod loader, utilizing NeoForge's RegisterCommandsEvent to register commands.
 */
@SuppressWarnings("unused")
public class MatthiesenLibNeoForgeCommandPlatformService implements MatthiesenLibCommandPlatform {
    /**
     * Default constructor for the MatthiesenLibNeoForgeCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     */
    public MatthiesenLibNeoForgeCommandPlatformService() {}

    /**
     * Default constructor for the MatthiesenLibNeoForgeCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     * @param registrationHandler The handler to invoke with the platform-specific CommandRegistrar.
     */
    @Override
    public void registerCommands(Consumer<MatthiesenLibCommandRegistrar> registrationHandler) {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()))
        );
    }
}


