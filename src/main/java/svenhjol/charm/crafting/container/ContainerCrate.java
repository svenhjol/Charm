package svenhjol.charm.crafting.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.crafting.tile.TileCrate;
import svenhjol.meson.MesonContainer;

@ChestContainer
public class ContainerCrate extends MesonContainer
{
    public ContainerCrate(InventoryPlayer playerInv, TileCrate crate)
    {
        super(playerInv, crate);

        IItemHandler inventory = crate.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (inventory == null) return;

        // 9 slots for the crate
        int x = 8;
        int y = 35;
        int slotIndex = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(inventory, slotIndex, x, y) {
                @Override
                public void onSlotChanged()
                {
                    crate.markDirty();
                }
            });
            slotIndex++;
            x += 18;
        }

        // these are the 36 slots of the player's inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // slots for the hotbar
        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        player.playSound(CharmSounds.WOOD_CLOSE, 1.0f, 1.0f);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}