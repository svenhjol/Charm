package svenhjol.charm.screenhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import svenhjol.charm.module.Crates;

public class CrateSlot extends Slot {
    public CrateSlot(Inventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return Crates.canCrateInsertItem(stack);
    }
}
