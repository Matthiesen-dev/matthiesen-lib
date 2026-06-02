package dev.matthiesen.fabric.matthiesen_lib_api.permission;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPermissionValidator;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Implementation of the PermissionValidator interface that checks permissions using the fabric-permissions-api.
 * This validator will check if a player has the required permission level to execute a command or perform an action
 * using the fabric-permissions-api, which allows for integration with various permissions mods that support the API.
 * For more information on the fabric-permissions-api. See <a href="https://github.com/lucko/fabric-permissions-api">...</a>
 */
public class MatthiesenLibFabricMatthiesenLibPermissionValidator implements MatthiesenLibPermissionValidator {
    /**
     * Creates a new instance of the FabricPermissionValidator. This constructor does not perform any initialization,
     * as the initialize method is called separately when the validator is registered.
     */
    public MatthiesenLibFabricMatthiesenLibPermissionValidator() {}

    @Override
    public void initialize() {
        MatthiesenLibApiConstants.createExtendedLog("Booting FabricPermissionValidator, permissions will be checked using fabric-permissions-api, see https://github.com/lucko/fabric-permissions-api");
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
