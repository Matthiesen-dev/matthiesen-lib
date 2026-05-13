package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.command.AbstractCommand;
import dev.matthiesen.common.matthiesen_lib.interfaces.CommandRegistrar;
import dev.matthiesen.common.matthiesen_lib.platform.CommonCommandPlatform;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class NeoForgeCommandPlatformService implements CommonCommandPlatform {
    @Override
    public void registerCommands(Consumer<CommandRegistrar> registrationHandler) {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                registrationHandler.accept((AbstractCommand command) -> command.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()))
        );
    }
}


