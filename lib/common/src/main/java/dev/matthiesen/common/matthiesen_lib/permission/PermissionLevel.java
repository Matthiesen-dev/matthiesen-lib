package dev.matthiesen.common.matthiesen_lib.permission;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel} instead. */
@SuppressWarnings("unused")
@Deprecated
public enum PermissionLevel {
    NONE(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.NONE),
    SPAWN_PROTECTION_BYPASS(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.SPAWN_PROTECTION_BYPASS),
    CHEAT_COMMANDS_AND_COMMAND_BLOCKS(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS),
    MULTIPLAYER_MANAGEMENT(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.MULTIPLAYER_MANAGEMENT),
    ALL_COMMANDS(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.ALL_COMMANDS);

    private final dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel apiLevel;

    PermissionLevel(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel apiLevel) {
        this.apiLevel = apiLevel;
    }

    public int getNumericalValue() {
        return apiLevel.getNumericalValue();
    }

    public dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel toApi() {
        return apiLevel;
    }

    public static PermissionLevel byNumericValue(int value) {
        return fromApi(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel.byNumericValue(value));
    }

    public static PermissionLevel fromApi(dev.matthiesen.api.matthiesen_lib_api.permission.PermissionLevel level) {
        for (PermissionLevel permissionLevel : values()) {
            if (permissionLevel.apiLevel == level) {
                return permissionLevel;
            }
        }
        throw new IllegalArgumentException("No PermissionLevel mapping for: " + level);
    }
}
