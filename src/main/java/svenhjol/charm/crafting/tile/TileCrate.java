package svenhjol.charm.crafting.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.meson.MesonTileInventory;
import vazkii.quark.api.IDropoffManager;

@Optional.Interface(iface = "vazkii.quark.api.IDropoffManager", modid = "quark")
public class TileCrate extends MesonTileInventory implements IDropoffManager
{
    protected boolean showName;

    public static final int SIZE = 9;
    public ItemStackHandler inventory = new ItemStackHandler(getInventorySize())
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return Crate.canInsertItem(stack) ? 0 : 64;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileCrate.this.markDirty();
        }
    };

    @Override
    public boolean acceptsDropoff(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void readTag(NBTTagCompound tag)
    {
        showName = tag.getBoolean("showname");
        super.readTag(tag);
    }

    @Override
    public void writeTag(NBTTagCompound tag)
    {
        tag.setBoolean("showname", showName);
        super.writeTag(tag);
    }

    public void setShowName(boolean show)
    {
        this.showName = show;
    }

    @Override
    public String getDefaultName()
    {
        return I18n.translateToLocal("tile.charm:crate.name");
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return showName ? new TextComponentString(getName()) : null;
    }

    @Override
    public int getInventorySize()
    {
        return SIZE;
    }
}