package svenhjol.charm.decoration.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import svenhjol.charm.decoration.module.Crates;

public class CrateSlot extends Slot {
    public CrateSlot(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return Crates.canInsertItem(stack);
    }
}
