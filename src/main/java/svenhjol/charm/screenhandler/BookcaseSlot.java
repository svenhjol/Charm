package svenhjol.charm.screenhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import svenhjol.charm.module.Bookcases;

public class BookcaseSlot extends Slot {
    public BookcaseSlot(Inventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return Bookcases.canContainItem(stack);
    }
}
