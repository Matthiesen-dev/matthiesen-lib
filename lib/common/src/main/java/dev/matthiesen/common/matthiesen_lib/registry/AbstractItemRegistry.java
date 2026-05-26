package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.world.item.Item;

/**
 * Convenience base class for registries that register {@link Item} instances.
 *
 * <p>This type locks registration to the item registry category by wiring
 * {@link SupportedRegistries#ITEM} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractItemRegistry extends AbstractRegistry<Item> {
    /**
     * Creates an item registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractItemRegistry(String modId) {
        super(modId, SupportedRegistries.ITEM);
    }

    /**
     * Creates an item registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform item registrations
     */
    protected AbstractItemRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.ITEM);
    }
}

