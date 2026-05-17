package dev.matthiesen.common.matthiesen_lib.utility;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Utility class for running slash commands on the Minecraft server. Provides methods to execute commands as either the server or a specific player.
 */
@SuppressWarnings("unused")
public class RunSlashCommand {
    /**
     * Default constructor for RunSlashCommand. This class is not meant to be instantiated, so the constructor is private to prevent instantiation.
     */
    private RunSlashCommand() {}

    /**
     * Executes a slash command as the server. This method allows you to run any command with the server's permissions and context.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asServer(String command) {
        MinecraftServer server = MatthiesenLib.getMinecraftServer();
        if (server == null) {
            MatthiesenLibConstants.createErrorLog("Cannot run command as server because the server instance is not available.");
            return;
        }
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }

    /**
     * Executes a slash command as the server using a provided MinecraftServer instance. This method allows you to run any
     * command with the server's permissions and context.
     * @param server The MinecraftServer instance on which to execute the command. This allows you to specify a server instance
     *               directly, which can be useful in certain contexts where the server is already available.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asServer(MinecraftServer server, String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }

    /**
     * Executes a slash command as a specific player. This method allows you to run any command with the player's permissions and context.
     * @param player The ServerPlayer instance representing the player as whom the command will be executed. The command will
     *               be executed with this player's permissions and context.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asPlayer(ServerPlayer player, String command) {
        MinecraftServer server = MatthiesenLib.getMinecraftServer();
        if (server == null) {
            MatthiesenLibConstants.createErrorLog("Cannot run command as player because the server instance is not available.");
            return;
        }
        server.getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
    }

    /**
     * Executes a slash command as a specific player using a provided MinecraftServer instance. This method allows you to run
     * any command with the player's permissions and context.
     * @param server The MinecraftServer instance on which to execute the command. This allows you to specify a server instance
     *               directly, which can be useful in certain contexts where the server is already available.
     * @param player The ServerPlayer instance representing the player as whom the command will be executed. The command will
     *               be executed with this player's permissions and context.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asPlayer(MinecraftServer server, ServerPlayer player, String command) {
        server.getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
    }
}
