package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommon;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class SoundRegistry {
    public static void init() {}

    @SuppressWarnings("SameParameterValue")
    private static <T extends SoundEvent> Supplier<T> register(String id, Supplier<T> sound) {
        return ExampleModCommon.COMMON_PLATFORM.registerSound(id, sound);
    }
}
