package svenhjol.charm.world.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonTile;

import javax.annotation.Nullable;

public class TileRunePortal extends TileEntityEndPortal implements IMesonTile
{
    public BlockPos portal;

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
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
        if (this.portal != null) {
            tag.setLong("portal", this.portal.toLong());
        }
    }

    public void readTag(NBTTagCompound tag)
    {
        this.portal = BlockPos.fromLong(tag.getLong("portal"));
    }
}
