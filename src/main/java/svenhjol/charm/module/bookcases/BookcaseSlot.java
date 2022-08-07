package svenhjol.charm.module.bookcases;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BookcaseSlot extends Slot {
    public BookcaseSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Bookcases.isValidItem(stack);
    }
}
