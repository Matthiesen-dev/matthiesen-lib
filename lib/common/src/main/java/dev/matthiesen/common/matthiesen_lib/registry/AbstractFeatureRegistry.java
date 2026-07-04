package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.world.level.levelgen.feature.Feature;

/**
 * Abstract registry for features, providing a base implementation for registering features with a mod ID or an existing {@link MatthiesenLib.RegistryBuilder}.
 */
@SuppressWarnings("unused")
public abstract class AbstractFeatureRegistry extends AbstractRegistry<Feature<?>> {
    /**
     * Creates an entity effect registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractFeatureRegistry(String modId) {
        super(modId, SupportedRegistries.FEATURE);
    }

    /**
     * Creates an entity effect registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform entity effect registrations
     */
    protected AbstractFeatureRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.FEATURE);
    }
}
