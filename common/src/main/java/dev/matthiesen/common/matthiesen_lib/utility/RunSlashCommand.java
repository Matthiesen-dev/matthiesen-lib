package dev.matthiesen.common.matthiesen_lib.utility;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Utility class for running slash commands on the Minecraft server. Provides methods to execute commands as either the server or a specific player.
 */
@SuppressWarnings("unused")
public class RunSlashCommand {
    /**
     * Executes a slash command as the server. This method allows you to run any command with the server's permissions and context.
     * @param server The MinecraftServer instance on which to execute the command.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asServer(MinecraftServer server, String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }

    /**
     * Executes a slash command as a specific player. This method allows you to run any command with the permissions and context of the given player.
     * @param server The MinecraftServer instance on which to execute the command.
     * @param player The ServerPlayer instance representing the player as whom the command should be executed.
     * @param command The command string to execute, without the leading slash. For example, "give @s minecraft:diamond 64".
     */
    public static void asPlayer(MinecraftServer server, ServerPlayer player, String command) {
        server.getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
    }
}
