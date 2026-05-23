package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;

/** @deprecated Use {@link dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibPermissionsManager} instead. */
@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public final class MatthiesenLibPermissionsManager {
    @SuppressWarnings("unused")
    private static final Runnable KEEP_MOD_INITIALIZER = MatthiesenLibPermissionsManager::modInitializer;
    @SuppressWarnings("unused")
    private static final java.util.function.Consumer<Permission> KEEP_REGISTER_PERMISSION = MatthiesenLibPermissionsManager::registerPermission;
    @SuppressWarnings("unused")
    private static final java.util.function.Supplier<java.util.List<Permission>> KEEP_ALL = MatthiesenLibPermissionsManager::all;
    @SuppressWarnings("unused")
    private static final java.util.function.IntSupplier KEEP_PENDING_COUNT = MatthiesenLibPermissionsManager::getPendingPermissionCount;

    static {
        if (Boolean.getBoolean("matthiesen-lib.compat.keepAlive")) {
            registerPermission(null);
            int ignored = getPendingPermissionCount();
            if (ignored == Integer.MIN_VALUE) {
                throw new AssertionError();
            }
        }
    }

    private MatthiesenLibPermissionsManager() {}

    @SuppressWarnings("unused")
    public static void modInitializer() {
        dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibPermissionsManager.modInitializer();
    }

    @SuppressWarnings("unused")
    public static void registerPermission(Permission permission) {
        dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibPermissionsManager.registerPermission(permission);
    }

    @SuppressWarnings("unused")
    public static java.util.List<Permission> all() {
        return dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibPermissionsManager.all();
    }

    @SuppressWarnings("unused")
    public static int getPendingPermissionCount() {
        return dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibPermissionsManager.getPendingPermissionCount();
    }
}
