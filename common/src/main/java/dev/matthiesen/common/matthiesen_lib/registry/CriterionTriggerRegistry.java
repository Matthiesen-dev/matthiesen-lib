package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommon;
import net.minecraft.advancements.CriterionTrigger;

import java.util.function.Supplier;

public class CriterionTriggerRegistry {
    public static void init() {}

    private static <T extends CriterionTrigger<?>>Supplier<T> register(String id, Supplier<T> criterionTrigger) {
        return ExampleModCommon.COMMON_PLATFORM.registerCriteriaTriggers(id, criterionTrigger);
    }
}
