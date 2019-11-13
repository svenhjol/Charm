package svenhjol.charm.world.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.world.module.EndPortalRunes;

public class RunePortalTileEntity extends TileEntity
{
    public BlockPos portal;

    public RunePortalTileEntity()
    {
        super(EndPortalRunes.tile);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderFace(Direction face) {
        return face == Direction.UP;
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
    }

    protected CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (portal != null) {
            tag.putLong("portal", portal.toLong());
        }
        return tag;
    }
}
