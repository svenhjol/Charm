package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class MesonBlockTE<TE extends TileEntity> extends MesonBlock
{
    public MesonBlockTE(Block.Properties props)
    {
        super(props);
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
