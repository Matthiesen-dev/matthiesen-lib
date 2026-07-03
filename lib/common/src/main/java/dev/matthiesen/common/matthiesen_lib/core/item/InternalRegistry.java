package dev.matthiesen.common.matthiesen_lib.core.item;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.registry.AbstractItemRegistry;

import java.util.function.Supplier;

public final class InternalRegistry extends AbstractItemRegistry {
    private static final InternalRegistry INSTANCE = new InternalRegistry();

    private InternalRegistry() {
        super(MatthiesenLibConstants.MOD_ID);
    }

    public static void init() {}

    public static final Supplier<CreativeTabSectionHeaderItem> CREATIVE_TAB_SECTION_HEADER_ITEM =
            INSTANCE.register("section_header", CreativeTabSectionHeaderItem::new);
}
