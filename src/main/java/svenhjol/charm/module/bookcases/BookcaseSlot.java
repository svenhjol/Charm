package svenhjol.charm.module.bookcases;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.module.bookcases.Bookcases;

public class BookcaseSlot extends Slot {
    public BookcaseSlot(Container inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Bookcases.canContainItem(stack);
    }
}
