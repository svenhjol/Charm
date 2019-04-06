package svenhjol.charm.crafting.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import svenhjol.charm.base.CharmSounds;
import svenhjol.meson.MesonContainer;
import svenhjol.charm.crafting.tile.TileBookshelfChest;

public class ContainerBookshelfChest extends MesonContainer
{
    protected TileEntity tile;

    public ContainerBookshelfChest(InventoryPlayer inv, TileBookshelfChest chest)
    {
        this.tile = chest;
        IItemHandler inventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (inventory == null) return;

        // 9 flots for the chest
        int x = 8;
        int y = 35;
        int slotIndex = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(inventory, slotIndex, x, y) {
                @Override
                public void onSlotChanged()
                {
                    chest.markDirty();
                }
            });
            slotIndex++;
            x += 18;
        }

        // these are the 36 slots of the player's inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }


        // slots for the hotbar
        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        player.playSound(CharmSounds.BOOKSHELF_CLOSE, 1.0f, 1.0f);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
