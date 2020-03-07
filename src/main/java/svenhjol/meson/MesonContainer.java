package svenhjol.meson;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import svenhjol.meson.iface.IMesonContainer;

public abstract class MesonContainer extends Container implements IMesonContainer {
    protected final IInventory inventory;

    protected MesonContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory) {
        super(type, id);
        this.inventory = inventory;
        inventory.openInventory(playerInventory.player);
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.inventory.closeInventory(player);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.inventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (index < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, this.inventory.getSizeInventory(), this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;

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
}
