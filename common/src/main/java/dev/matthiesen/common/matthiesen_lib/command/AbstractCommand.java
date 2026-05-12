package dev.matthiesen.common.matthiesen_lib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Base class for commands. Extend this and implement the register method to create a new command.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommand {
    /**
     * Function to register the command. This is called by the CommandManager when the command is being registered.
     */
    public abstract void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context);

    /**
     * Function to execute when the command is run. This is called by the CommandManager when the command is executed.
     */
    public abstract int action(CommandContext<CommandSourceStack> context);
}
