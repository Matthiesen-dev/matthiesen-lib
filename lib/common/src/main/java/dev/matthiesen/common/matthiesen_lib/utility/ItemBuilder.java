package dev.matthiesen.common.matthiesen_lib.utility;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib_api.utility.ItemBuilder} instead. */
@Deprecated
@SuppressWarnings("unused")
public class ItemBuilder extends dev.matthiesen.api.matthiesen_lib_api.utility.ItemBuilder {
    public ItemBuilder(Item item) {
        super(item);
    }

    public ItemBuilder(ItemStack item) {
        super(item);
    }
}
