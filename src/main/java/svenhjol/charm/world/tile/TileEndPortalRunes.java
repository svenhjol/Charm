package svenhjol.charm.world.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import svenhjol.charm.Charm;
import svenhjol.charm.world.block.BlockEndPortalFrameRunes;
import svenhjol.charm.world.feature.EndPortalRunes;
import svenhjol.meson.MesonTile;

public class TileEndPortalRunes extends MesonTile
{
    public int facing;

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

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

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing.getIndex();
        this.markDirty();
    }

    public void updateBlock()
    {
        IBlockState current = world.getBlockState(pos);
        if (current.getBlock() == EndPortalRunes.portalFrame) {

            IBlockState state = current
                .withProperty(BlockEndPortalFrameRunes.FACING, current.getValue(BlockEndPortalFrameRunes.FACING))
                .withProperty(BlockEndPortalFrameRunes.COLOR, current.getValue(BlockEndPortalFrameRunes.COLOR))
                ;
            world.setBlockState(pos, state, 2);
        }
    }

    public EnumFacing getFacing()
    {
        return EnumFacing.byIndex(this.facing);
    }
}
