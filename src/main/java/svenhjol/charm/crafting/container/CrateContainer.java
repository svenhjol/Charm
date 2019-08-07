package svenhjol.charm.crafting.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CrateContainer extends Container
{
    private final IInventory inventory;

    public CrateContainer(int id, PlayerInventory player)
    {
        this(id, player, new Inventory(9));
    }

    public CrateContainer(int id, PlayerInventory player, IInventory inventory)
    {
        super(ContainerType.GENERIC_9X1, id);
        this.inventory = inventory;

        inventory.openInventory(player.player);

        int index = 0;

        // crate's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, index++, 8 + (i * 18), 35));
        }

        index = 9;

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 51 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 109));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (slotIndex < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stackInSlot, 0, this.inventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return stack;
    }

    @Override
    public void onContainerClosed(PlayerEntity player)
    {
        super.onContainerClosed(player);
        this.inventory.closeInventory(player);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.inventory.isUsableByPlayer(playerIn);
    }
}
