package svenhjol.meson;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.meson.iface.IMesonTile;

import javax.annotation.Nullable;

public abstract class MesonTile extends TileEntity implements IMesonTile
{
    protected String name;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        NBTTagCompound nbt = super.writeToNBT(tag);
        writeTag(tag);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        readTag(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeTag(tag);
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        writeTag(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        super.handleUpdateTag(tag);
        readTag(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readTag(pkt.getNbtCompound());
    }

    public void writeTag(NBTTagCompound tag)
    {
        // hook for TEs to do things with nbt data
        tag.setString("name", getName());
    }

    public void readTag(NBTTagCompound tag)
    {
        // hook for TEs to do things with nbt data
        name = tag.getString("name");
    }

    public String getDefaultName()
    {
        return "";
    }

    public String getName()
    {
        return hasCustomName() ? name : getDefaultName();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean hasCustomName()
    {
        return name != null && !name.isEmpty();
    }
}
