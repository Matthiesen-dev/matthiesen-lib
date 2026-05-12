package dev.matthiesen.common.matthiesen_lib.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple menu that has no inventory. This is useful for GUIs that don't need to interact with the player's inventory,
 * such as a crafting table or furnace GUI. This menu will not allow the player to move items in or out of the menu, and it will not have any slots.
 * It can be used as a base class for GUIs that don't need an inventory,
 */
@SuppressWarnings("unused")
public abstract class AbstractNoInventoryMenu extends AbstractContainerMenu {
    protected AbstractNoInventoryMenu(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
