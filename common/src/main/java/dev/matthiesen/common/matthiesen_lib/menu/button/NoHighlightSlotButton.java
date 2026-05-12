package dev.matthiesen.common.matthiesen_lib.menu.button;

import net.minecraft.world.Container;

/**
 * A slot button that cannot be highlighted when hovered over. This is useful for buttons in GUIs that should not have a highlight effect when hovered over, such
 * as buttons that are purely decorative or that should not draw attention to themselves. This slot will still render the item in the slot and will still call the onTake
 * method when the player clicks on the slot, so it can be used to trigger actions when the button is clicked without drawing attention to itself.
 */
@SuppressWarnings("unused")
public class NoHighlightSlotButton extends SlotButton{
    public NoHighlightSlotButton(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean isHighlightable() { return false; }
}
