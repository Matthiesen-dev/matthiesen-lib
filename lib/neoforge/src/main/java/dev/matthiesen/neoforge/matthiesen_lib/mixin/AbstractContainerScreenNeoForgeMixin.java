package dev.matthiesen.neoforge.matthiesen_lib.mixin;

import dev.matthiesen.common.matthiesen_lib.core.item.CreativeTabSectionHeaderItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * NeoForge-specific counterpart to AbstractContainerScreenMixin.
 * NeoForge 21.1.x patches AbstractContainerScreen#render to call the new instance method
 * {@code renderSlotHighlight(GuiGraphics, Slot, int, int, float)} per slot instead of the
 * vanilla static {@code renderSlotHighlight(GuiGraphics, int, int, int)}.
 * The common mixin's @Redirect cannot find its call-site on NeoForge (hence require=0 there).
 * This mixin provides the equivalent behaviour by injecting into the NeoForge instance method.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenNeoForgeMixin {

    /**
     * Suppress the slot-hover highlight for CreativeTabSectionHeaderItem slots.
     * The {@code slot} parameter is the exact slot NeoForge is about to highlight,
     * so we only need to check its item stack — no shadow field access required.
     */
    @Inject(
            method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;IIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void skipSectionHeaderHoverHighlight(
            GuiGraphics guiGraphics, Slot slot, int x, int y, float partialTick, CallbackInfo ci) {
        if (slot != null && slot.getItem().getItem() instanceof CreativeTabSectionHeaderItem) {
            ci.cancel();
        }
    }
}

