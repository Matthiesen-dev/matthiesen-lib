package dev.matthiesen.fabric.matthiesen_lib.permission;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.interfaces.Permission;
import dev.matthiesen.common.matthiesen_lib.interfaces.PermissionValidator;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class FabricPermissionValidator implements PermissionValidator {
    @Override
    public void initialize() {
        Constants.createInfoLog("Booting FabricPermissionValidator, permissions will be checked using fabric-permissions-api, see https://github.com/lucko/fabric-permissions-api");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        return Permissions.check(player, permission.getLiteral(), permission.getLevel().getNumericalValue());
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        return Permissions.check(source, permission.getLiteral(), permission.getLevel().getNumericalValue());
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return Permissions.check(player, permission, level);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        return Permissions.check(source, permission, level);
    }
}
