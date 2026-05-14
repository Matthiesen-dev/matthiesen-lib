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
     * Default constructor for the AbstractCommand class. No initialization is required as setup is handled in the register method.
     */
    public AbstractCommand() {}

    /**
     * Function to register the command. This is called by the CommandManager when the command is being registered.
     * @param dispatcher The command dispatcher to register the command with.
     * @param registry The command build context to register the command with.
     * @param context The command selection context to register the command with.
     */
    public abstract void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context);

    /**
     * Function to execute when the command is run. This is called by the CommandManager when the command is executed.
     * @param context The command context containing information about the command execution.
      * @return The result of the command execution. This is used to determine if the command was successful or not. A return value of 0 indicates failure, while a positive value indicates success.
     */
    public abstract int action(CommandContext<CommandSourceStack> context);
}
