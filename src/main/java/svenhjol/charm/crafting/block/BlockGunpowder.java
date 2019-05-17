package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.GunpowderBlock;
import svenhjol.meson.iface.IMesonBlock;

public class BlockGunpowder extends BlockFalling implements IMesonBlock
{
    protected String name;

    public BlockGunpowder()
    {
        this.name = "gunpowder_block";
        register(name);
        setSoundType(SoundType.SAND);
        setHardness(GunpowderBlock.hardness);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    protected boolean tryTouchLava(World world, BlockPos pos, IBlockState state)
    {
        boolean flag = false;

        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != EnumFacing.DOWN) {
                BlockPos blockPos = pos.offset(facing);
                if (world.getBlockState(blockPos).getMaterial() == Material.LAVA) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
            world.setBlockToAir(pos);
        }

        return flag;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        if (!tryTouchLava(world, pos, state)) {
            super.neighborChanged(state, world, pos, block, fromPos);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        if (!tryTouchLava(world, pos, state)) {
            super.onBlockAdded(world, pos, state);
        }
    }
}
