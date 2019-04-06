package svenhjol.charm.crafting.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonTileInventory;
import vazkii.quark.api.IDropoffManager;

public class TileBarrel extends MesonTileInventory implements IDropoffManager
{
    public static final int SIZE = 27;

    public ItemStackHandler inventory = new ItemStackHandler(getInventorySize())
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileBarrel.this.markDirty();
        }
    };

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    @Override
    public int getInventorySize()
    {
        return SIZE;
    }

    @Override
    public boolean acceptsDropoff(EntityPlayer player)
    {
        return true;
    }
}
