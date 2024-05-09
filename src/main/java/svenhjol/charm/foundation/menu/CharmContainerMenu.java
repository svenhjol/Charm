package svenhjol.charm.foundation.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public abstract class CharmContainerMenu extends AbstractContainerMenu {
    protected final Container container;

    protected CharmContainerMenu(MenuType<?> type, int id, Inventory playerInventory, Container container) {
        super(type, id);
        this.container = container;
        container.startOpen(playerInventory.player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var stack = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            var stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (index < container.getContainerSize()) {
                if (!moveItemStackTo(stackInSlot, container.getContainerSize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }
}
