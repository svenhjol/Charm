package svenhjol.charm.world.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import svenhjol.charm.Charm;
import svenhjol.charm.world.block.BlockRunePortalFrame;
import svenhjol.charm.world.feature.EndPortalRunes;
import svenhjol.meson.MesonTile;

public class TileRunePortalFrame extends MesonTile
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

    public EnumFacing getFacing()
    {
        EnumFacing facing = EnumFacing.byIndex(this.facing);
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.WEST;
        }
        return facing;
    }

    @Override
    public void onLoad()
    {
        if (world.getBlockState(pos).getBlock() == EndPortalRunes.frame) {
            IBlockState state = world.getBlockState(pos)
                .withProperty(BlockRunePortalFrame.FACING, getFacing());
            world.setBlockState(pos, state, 3);
        }
    }
}
