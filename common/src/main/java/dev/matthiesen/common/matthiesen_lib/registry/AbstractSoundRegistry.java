package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.sounds.SoundEvent;

/**
 * Convenience base class for registries that register {@link SoundEvent} instances.
 *
 * <p>This type locks registration to the sound registry category by wiring
 * {@link SupportedRegistries#SOUND} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractSoundRegistry extends AbstractRegistry<SoundEvent> {
    /**
     * Creates a sound registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractSoundRegistry(String modId) {
        super(modId, SupportedRegistries.SOUND);
    }

    /**
     * Creates a sound registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform sound registrations
     */
    protected AbstractSoundRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.SOUND);
    }
}

