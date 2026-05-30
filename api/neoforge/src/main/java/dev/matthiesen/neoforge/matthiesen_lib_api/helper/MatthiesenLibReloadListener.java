package dev.matthiesen.neoforge.matthiesen_lib_api.helper;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class MatthiesenLibReloadListener extends SimplePreparableReloadListener<Void> {
    private final Supplier<Map<String, Runnable>> getter;

    public MatthiesenLibReloadListener(Supplier<Map<String, Runnable>> getter) {
        this.getter = getter;
    }

    @Override
    protected @NotNull Void prepare(@NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        return null;
    }

    @Override
    protected void apply(@NotNull Void object, @NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        Map<String, Runnable> runnables = getter.get();
        if (runnables.isEmpty()) return;
        for (Map.Entry<String, Runnable> entry : runnables.entrySet()) {
            try {
                MatthiesenLibApiConstants.createInfoLog("Executing reload runnable for mod: " + entry.getKey());
                entry.getValue().run();
            } catch (Exception e) {
                MatthiesenLibApiConstants.createErrorLog("Error executing reload runnable for mod: " + entry.getKey(), e);
            }
        }
    }
}
