package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.world.item.CreativeModeTab;

/**
 * Convenience base class for registries that register {@link CreativeModeTab} instances.
 *
 * <p>This type locks registration to the creative mode tab registry category by wiring
 * {@link SupportedRegistries#CREATIVE_MODE_TAB} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractCreativeModeTabRegistry extends AbstractRegistry<CreativeModeTab> {
    /**
     * Creates a creative mode tab registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractCreativeModeTabRegistry(String modId) {
        super(modId, SupportedRegistries.CREATIVE_MODE_TAB);
    }

    /**
     * Creates a creative mode tab registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform creative mode tab registrations
     */
    protected AbstractCreativeModeTabRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.CREATIVE_MODE_TAB);
    }
}

