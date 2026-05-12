package dev.matthiesen.common.matthiesen_lib.menu.button;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A simple slot that cannot be interacted with. This is useful for buttons in GUIs, as it prevents the player from accidentally moving items into or out of the slot.
 * The slot will still render the item in the slot, so it can be used to display an item as part of the button's appearance. The slot will also still call the onTake
 * method when the player clicks on the slot, so it can be used to trigger actions when the button is
 */
@SuppressWarnings("unused")
public class SlotButton extends Slot {
    public SlotButton(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPickup(Player p) { return false; }

    @Override
    public boolean mayPlace(ItemStack s) { return false; }
}
