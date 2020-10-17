package svenhjol.charm.base.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class CharmScreenHandler extends ScreenHandler implements ICharmScreenHandler {
    protected final Inventory inventory;

    protected CharmScreenHandler(ScreenHandlerType<?> type, int id, PlayerInventory playerInventory, Inventory inventory) {
        super(type, id);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if (index < this.inventory.size()) {

                if (!this.insertItem(stackInSlot, this.inventory.size(), this.slots.size(), true))
                    return ItemStack.EMPTY;

            } else if (!this.insertItem(stackInSlot, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return stack;
    }
}
