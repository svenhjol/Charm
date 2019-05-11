package svenhjol.charm.smithing.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

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
        return FurnaceRecipes.instance().getSmeltingResult(stack) != ItemStack.EMPTY;
    }
}
