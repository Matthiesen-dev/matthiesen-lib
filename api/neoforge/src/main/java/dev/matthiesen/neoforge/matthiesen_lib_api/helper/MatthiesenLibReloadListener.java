package dev.matthiesen.neoforge.matthiesen_lib_api.helper;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A reload listener that runs a map of runnables on resource reload. The map is supplied by a supplier, allowing it to be dynamic and change over time.
 * This class is used to execute custom code during resource reloads, such as when data packs are reloaded or when the /reload command is executed.
 * Mods can register their own runnables to be executed during these events by providing a supplier that returns a map of mod IDs to runnables.
 * The runnables will be executed in the order they are returned by the supplier, and any exceptions thrown by the runnables will be caught and
 * logged to prevent them from disrupting the reload process. This allows mods to perform custom actions during reloads, such as reloading
 * configurations or refreshing data, without needing to directly integrate with the reload system of the mod loader. Mods should ensure that their
 * runnables are thread-safe and do not perform long-running operations to avoid blocking the main thread during a reload.
 */
public final class MatthiesenLibReloadListener extends SimplePreparableReloadListener<Void> {
    private final Supplier<Map<String, Runnable>> getter;

    /**
     * Constructs a new MatthiesenLibReloadListener with the specified supplier for the map of runnables. The supplier allows the map to be dynamic
     * and change over time,
     * providing flexibility for mods to register and unregister their reload runnables as needed. The supplier will be called each time a reload
     * occurs, allowing the map to be updated dynamically.
     * @param getter A supplier that provides a map of mod IDs to runnables to execute during a reload. The supplier will be called each time a
     *               reload occurs, allowing the map to be updated dynamically.
     */
    public MatthiesenLibReloadListener(Supplier<Map<String, Runnable>> getter) {
        this.getter = getter;
    }

    /**
     * Prepares the reload listener for a resource reload. This method is called before the actual reload process begins and can be used to
     * perform any necessary setup or preparation before the runnables are executed.
     * @param arg The resource manager that is being reloaded. This can be used to access the resources that are being reloaded and perform
     *            any necessary setup based on the resources being reloaded.
     * @param arg2 The profiler filler that can be used to profile the reload process. This can be used to measure the time taken by the
     *             preparation phase and identify any performance issues during the reload process.
     * @return This method returns null as the preparation phase does not produce any result that needs to be passed to the apply method.
     * The runnables will be executed in the apply method, which is called after the preparation phase is complete.
     */
    @Override
    protected @NotNull Void prepare(@NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        return null;
    }

    /**
     * Applies the reload listener by executing the runnables provided by the supplier. This method is called after the preparation phase is
     * complete and is responsible for executing the registered runnables during a reload. The runnables are executed in the order they are
     * returned by the supplier, and any exceptions thrown by the runnables are caught and logged to prevent them from disrupting the reload
     * process. This allows mods to perform custom actions during reloads, such as reloading configurations or refreshing data, without needing
     * to directly integrate with the reload system of the mod loader. Mods should ensure that their runnables are thread-safe and do not perform
     * long-running operations to avoid blocking the main thread during a reload.
     * @param object The object returned by the prepare method, which is null in this case as the preparation phase does not produce any result
     *               that needs to be passed to the apply method. The runnables will be executed based on the map provided by the supplier, which
     *               is accessed within this method.
     * @param arg The resource manager that is being reloaded. This can be used to access the resources that are being reloaded and perform any
     *            necessary actions based on the resources being reloaded during the execution of the runnables.
     * @param arg2 The profiler filler that can be used to profile the execution of the runnables. This can be used to measure the time taken by
     *             each runnable and identify any performance issues during the execution of the runnables in the reload process.
     */
    @Override
    protected void apply(@NotNull Void object, @NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        Map<String, Runnable> runnables = getter.get();
        if (runnables.isEmpty()) return;
        for (Map.Entry<String, Runnable> entry : runnables.entrySet()) {
            try {
                MatthiesenLibApiConstants.createExtendedLog("Executing reload runnable for mod: " + entry.getKey());
                entry.getValue().run();
            } catch (Exception e) {
                MatthiesenLibApi.ERROR_TRACKER.trackError(e);
                MatthiesenLibApiConstants.createErrorLog("Error executing reload runnable for mod: " + entry.getKey(), e);
            }
        }
    }
}
