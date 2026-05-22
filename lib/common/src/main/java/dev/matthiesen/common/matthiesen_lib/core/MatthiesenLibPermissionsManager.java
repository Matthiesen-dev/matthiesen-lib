package dev.matthiesen.common.matthiesen_lib.core;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibPermissionsManager} instead. */
@Deprecated
@SuppressWarnings("unused")
public final class MatthiesenLibPermissionsManager {
    @SuppressWarnings("unused")
    private static final Runnable KEEP_MOD_INITIALIZER = MatthiesenLibPermissionsManager::modInitializer;
    @SuppressWarnings("unused")
    private static final java.util.function.Consumer<dev.matthiesen.api.matthiesen_lib.permission.Permission> KEEP_REGISTER_PERMISSION = MatthiesenLibPermissionsManager::registerPermission;
    @SuppressWarnings("unused")
    private static final java.util.function.Supplier<java.util.List<dev.matthiesen.api.matthiesen_lib.permission.Permission>> KEEP_ALL = MatthiesenLibPermissionsManager::all;
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
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibPermissionsManager.modInitializer();
    }

    @SuppressWarnings("unused")
    public static void registerPermission(dev.matthiesen.api.matthiesen_lib.permission.Permission permission) {
        dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibPermissionsManager.registerPermission(permission);
    }

    @SuppressWarnings("unused")
    public static java.util.List<dev.matthiesen.api.matthiesen_lib.permission.Permission> all() {
        return dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibPermissionsManager.all();
    }

    @SuppressWarnings("unused")
    public static int getPendingPermissionCount() {
        return dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibPermissionsManager.getPendingPermissionCount();
    }
}
