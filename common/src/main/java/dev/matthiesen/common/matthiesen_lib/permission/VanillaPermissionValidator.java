package dev.matthiesen.common.matthiesen_lib.permission;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.interfaces.Permission;
import dev.matthiesen.common.matthiesen_lib.interfaces.PermissionValidator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Implementation of the PermissionValidator interface that checks permissions using Minecraft's built-in permission level system.
 * This validator will check if a player has the required permission level to execute a command or perform an action.
 */
public class VanillaPermissionValidator implements PermissionValidator {
    @Override
    public void initialize() {
        Constants.createInfoLog("Booting VanillaPermissionValidator, permissions will be checked using Minecraft's permission level system, see https://minecraft.wiki/w/Permission_level");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        return player.hasPermissions(permission.getLevel().getNumericalValue());
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return player.hasPermissions(level);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        return source.hasPermission(permission.getLevel().getNumericalValue());
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        return source.hasPermission(level);
    }
}
