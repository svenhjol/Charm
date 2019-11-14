package svenhjol.charm.world.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.world.module.EndPortalRunes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RunePortalTileEntity extends TileEntity
{
    public BlockPos portal;
    public List<Integer> colors = new ArrayList<>();

    public RunePortalTileEntity()
    {
        super(EndPortalRunes.tile);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        loadFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        CompoundNBT nbt = super.write(tag);
        return writeToNBT(nbt);
    }

    protected void loadFromNBT(CompoundNBT tag)
    {
        this.portal = BlockPos.fromLong(tag.getLong("portal"));
        this.colors = Arrays.stream(tag.getIntArray("colors")).boxed().collect(Collectors.toList());
    }

    protected CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (portal != null) {
            tag.putLong("portal", portal.toLong());
        }
        if (colors != null) {
            tag.putIntArray("colors", colors.stream().mapToInt(i -> i).toArray());
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }
}
