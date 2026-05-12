package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommon;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockRegistry {
    public static void init() {}

    // Registration
    private static <T extends Block> Supplier<T> register(String id, Supplier<T> block) {
        return ExampleModCommon.COMMON_PLATFORM.registerBlock(id, block);
    }
}
