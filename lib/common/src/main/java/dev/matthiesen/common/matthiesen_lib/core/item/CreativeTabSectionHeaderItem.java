package dev.matthiesen.common.matthiesen_lib.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class CreativeTabSectionHeaderItem extends Item {
    private static final String SECTION_ID_KEY = "SectionID";
    private static final String CREATIVE_MODE_TAB_ID = "CreativeModeTabID";
    private static final String ROLE_KEY = "SectionRole";
    private static final String ROLE_HEADER = "header";
    private static final String ROLE_PLACEHOLDER = "placeholder";

    public CreativeTabSectionHeaderItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static ItemStack createHeaderStack(Item item, ResourceLocation creativeModeTabId, ResourceLocation sectionId) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putString(CREATIVE_MODE_TAB_ID, creativeModeTabId.toString());
        tag.putString(SECTION_ID_KEY, sectionId.toString());
        tag.putString(ROLE_KEY, ROLE_HEADER);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static ItemStack createPlaceholderStack(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putString(ROLE_KEY, ROLE_PLACEHOLDER);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static boolean isPlaceholder(ItemStack stack) {
        return ROLE_PLACEHOLDER.equals(getRole(stack));
    }

    // Used by the mixin to identify section rows and extract custom section data.
    public static ResourceLocation getSectionId(ItemStack stack) {
        String sectionId = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag()
                .getString(SECTION_ID_KEY);
        if (sectionId.isBlank()) {
            return null;
        }
        try {
            return ResourceLocation.parse(sectionId);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static ResourceLocation getCreativeModeTabId(ItemStack stack) {
        String tabId = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag()
                .getString(CREATIVE_MODE_TAB_ID);
        if (tabId.isBlank()) {
            return null;
        }
        try {
            return ResourceLocation.parse(tabId);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static String getRole(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag()
                .getString(ROLE_KEY);
    }
}
