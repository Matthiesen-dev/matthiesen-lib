package dev.matthiesen.common.matthiesen_lib.core.item;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.registry.AbstractItemRegistry;

import java.util.function.Supplier;

/**
 * The InternalRegistry class is a singleton that extends AbstractItemRegistry and is responsible for registering internal items used by the mod.
 * It provides a method to initialize the registry and a supplier for the Creative Tab Section Header Item. This class is designed to be used
 * internally within the mod and should not be instantiated directly.
 */
public final class InternalRegistry extends AbstractItemRegistry {
    private static final InternalRegistry INSTANCE = new InternalRegistry();

    private InternalRegistry() {
        super(MatthiesenLibConstants.MOD_ID);
    }

    /**
     * The init method is called to ensure that the registry is initialized and all items are registered.
     */
    public static void init() {}

    /**
     * The Creative Tab Section Header Item
     */
    public static final Supplier<CreativeTabSectionHeaderItem> CREATIVE_TAB_SECTION_HEADER_ITEM =
            INSTANCE.register("section_header", CreativeTabSectionHeaderItem::new);
}
