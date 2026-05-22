package dev.matthiesen.common.matthiesen_lib.permission;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib.permission.AbstractPermission} instead. */
@Deprecated
@SuppressWarnings("unused")
public abstract class AbstractPermission extends dev.matthiesen.api.matthiesen_lib.permission.AbstractPermission implements Permission {
    protected AbstractPermission(String node, PermissionLevel level) {
        super(node, level.toApi());
    }

    protected AbstractPermission(String node, dev.matthiesen.api.matthiesen_lib.permission.PermissionLevel level) {
        super(node, level);
    }
}
