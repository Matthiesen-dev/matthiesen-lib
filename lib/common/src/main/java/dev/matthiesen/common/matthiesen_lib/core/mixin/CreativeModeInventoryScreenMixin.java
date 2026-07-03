package dev.matthiesen.common.matthiesen_lib.core.mixin;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCreativeModeTabSectionsManager;
import dev.matthiesen.common.matthiesen_lib.core.item.CreativeTabSectionHeaderItem;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void blockSectionHeaderSlotClicks(Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if (slot != null && slot.getItem().getItem() instanceof CreativeTabSectionHeaderItem) {
            ci.cancel();
        }
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void injectSectionHeaders$selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        if (creativeModeTab == null) return;
        ResourceLocation selectedTabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(creativeModeTab);
        if (selectedTabId == null) return;
        if (MatthiesenLibCreativeModeTabSectionsManager.hasTabSections(selectedTabId)) {
            NonNullList<ItemStack> structuredItems = NonNullList.create();
            MatthiesenLibCreativeModeTabSectionsManager.getTabSections(selectedTabId).sections().entrySet().stream()
                    .sorted(Comparator.comparingInt((Map.Entry<ResourceLocation, List<ItemStack>> e) ->
                            MatthiesenLibCreativeModeTabSectionsManager.getTabSections(selectedTabId)
                                    .metadata()
                                    .get(e.getKey())
                                    .priority())
                            .reversed()
                    )
                    .forEach(entry -> {
                        ResourceLocation sectionId = entry.getKey();
                        ItemStack headerStack = CreativeTabSectionHeaderItem.createHeaderStack(
                                MatthiesenLibCreativeModeTabSectionsManager.CREATIVE_TAB_SECTION_HEADER_ITEM.get(),
                                selectedTabId,
                                sectionId
                        );
                        structuredItems.add(headerStack);
                        for (int i = 0; i < 8; i++) {
                            structuredItems.add(CreativeTabSectionHeaderItem.createPlaceholderStack(MatthiesenLibCreativeModeTabSectionsManager.CREATIVE_TAB_SECTION_HEADER_ITEM.get()));
                        }
                        structuredItems.addAll(entry.getValue());
                        while (structuredItems.size() % 9 != 0) {
                            structuredItems.add(ItemStack.EMPTY);
                        }
                    });
            if (((CreativeModeInventoryScreen) (Object) this).getMenu() instanceof ItemPickerMenuAccessor menuAccessor) {
                var menuItems = menuAccessor.getItemsList();
                menuItems.clear();
                menuItems.addAll(structuredItems);
                menuAccessor.invokeScrollTo(0.0f);
            }
        }
    }
}
