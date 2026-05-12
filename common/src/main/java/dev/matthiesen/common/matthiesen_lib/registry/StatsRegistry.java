package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommon;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class StatsRegistry {
    public static void init() {}

    private static <T extends ResourceLocation> Supplier<T> register(String id, Supplier<T> stats) {
        return ExampleModCommon.COMMON_PLATFORM.registerStats(id, stats);
    }
}
