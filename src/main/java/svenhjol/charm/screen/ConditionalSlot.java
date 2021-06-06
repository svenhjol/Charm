package svenhjol.charm.screen;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ConditionalSlot extends Slot {
    private final Predicate<ItemStack> condition;

    public ConditionalSlot(Predicate<ItemStack> condition, Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.condition = condition;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return condition.test(stack);
    }
}
