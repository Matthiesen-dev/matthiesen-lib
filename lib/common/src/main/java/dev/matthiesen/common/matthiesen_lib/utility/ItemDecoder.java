package dev.matthiesen.common.matthiesen_lib.utility;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib_api.utility.ItemDecoder} instead. */
@Deprecated
@SuppressWarnings("unused")
public class ItemDecoder {

    private ItemDecoder() {}

    public static Item stringToItem(String string, Item fallback) {
        return dev.matthiesen.api.matthiesen_lib_api.utility.ItemDecoder.stringToItem(string, fallback);
    }

    public static Block stringToBlock(String string, Block fallback) {
        return dev.matthiesen.api.matthiesen_lib_api.utility.ItemDecoder.stringToBlock(string, fallback);
    }
}
