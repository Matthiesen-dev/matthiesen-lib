package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.advancements.CriterionTrigger;

/**
 * Convenience base class for registries that register {@link CriterionTrigger} instances.
 *
 * <p>This type locks registration to the criteria trigger registry category by wiring
 * {@link SupportedRegistries#CRITERIA_TRIGGER} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractCriteriaTriggerRegistry extends AbstractRegistry<CriterionTrigger<?>> {
    /**
     * Creates a criteria trigger registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractCriteriaTriggerRegistry(String modId) {
        super(modId, SupportedRegistries.CRITERIA_TRIGGER);
    }

    /**
     * Creates a criteria trigger registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform criteria trigger registrations
     */
    protected AbstractCriteriaTriggerRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.CRITERIA_TRIGGER);
    }
}

