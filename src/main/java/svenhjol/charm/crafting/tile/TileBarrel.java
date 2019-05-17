package svenhjol.charm.crafting.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonTileInventory;
import vazkii.quark.api.IDropoffManager;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "vazkii.quark.api.IDropoffManager", modid = "quark")
public class TileBarrel extends MesonTileInventory implements IDropoffManager
{
    public static final int SIZE = 27;
    public int facing;

    public ItemStackHandler inventory = new ItemStackHandler(getInventorySize())
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            TileBarrel.this.markDirty();
        }
    };

    @Override
    public void readTag(NBTTagCompound tag)
    {
        facing = tag.getInteger("facing");
        super.readTag(tag);
    }

    @Override
    public void writeTag(NBTTagCompound tag)
    {
        tag.setInteger("facing", facing);
        super.writeTag(tag);
    }

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

    @Override
    public String getDefaultName()
    {
        return I18n.translateToLocal("tile.charm:barrel.name");
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing.getIndex();
        this.markDirty();
    }

    public EnumFacing getFacing()
    {
        return EnumFacing.byIndex(this.facing);
    }
}
