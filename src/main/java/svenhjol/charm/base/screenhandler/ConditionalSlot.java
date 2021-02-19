package svenhjol.charm.base.screenhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class ConditionalSlot extends Slot {
    private final Predicate<ItemStack> condition;

    public ConditionalSlot(Predicate<ItemStack> condition, Inventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.condition = condition;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return condition.test(stack);
    }
}
