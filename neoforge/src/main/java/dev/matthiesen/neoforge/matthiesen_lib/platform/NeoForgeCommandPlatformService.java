package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonCommandPlatform;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Consumer;

/**
 * Implementation of the CommonCommandPlatform interface for the NeoForge mod loader, utilizing NeoForge's RegisterCommandsEvent to register commands.
 */
@SuppressWarnings("unused")
public class NeoForgeCommandPlatformService implements CommonCommandPlatform {
    /**
     * Default constructor for the NeoForgeCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     */
    public NeoForgeCommandPlatformService() {}

    /**
     * Default constructor for the NeoForgeCommandPlatformService. No initialization is required as command registration is handled through the registerCommands method.
     * @param registrationHandler The handler to invoke with the platform-specific CommandRegistrar.
     */
    @Override
    public void registerCommands(Consumer<CommandRegistrar> registrationHandler) {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()))
        );
    }
}


