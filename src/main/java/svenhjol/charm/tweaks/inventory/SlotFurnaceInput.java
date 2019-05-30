package svenhjol.charm.tweaks.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.feature.RestrictFurnaceInput;

@SuppressWarnings("unused")
public class SlotFurnaceInput extends Slot
{
    public SlotFurnaceInput(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        if (Charm.hasFeature(RestrictFurnaceInput.class)) {
            return RestrictFurnaceInput.canSmelt(stack);
        }
        return true;
    }
}
