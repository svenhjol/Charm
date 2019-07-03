package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonBlock;

import java.util.Random;

public class BlockVariableRedstoneLight extends BlockRedstoneLight implements IMesonBlock
{
    public static PropertyInteger LIGHT = PropertyInteger.create("light", 0, 15);

    public BlockVariableRedstoneLight()
    {
        super(false);
        register();
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public String getName()
    {
        return "variable_redstone_lamp";
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        int power = worldIn.getRedstonePowerFromNeighbors(pos);
        updateLight(worldIn, pos, state, power);
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos)
    {
//        int power = world.getRedstonePowerFromNeighbors(changedBlockPos);
//        world.setLightFor(EnumSkyBlock.BLOCK, observerPos, power);
//        world.checkLight(observerPos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote && worldIn.isBlockPowered(pos)) {
            int power = worldIn.getRedstonePowerFromNeighbors(pos);
            updateLight(worldIn, pos, state, power);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIGHT);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && !worldIn.isBlockPowered(pos)) {
            updateLight(worldIn, pos, state, 0);
        }
    }

    private void updateLight(World world, BlockPos pos, IBlockState state, int power)
    {
        world.setLightFor(EnumSkyBlock.BLOCK, pos, power);
        world.setBlockState(pos, state.withProperty(LIGHT, power), 3);
        world.checkLight(pos);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LIGHT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LIGHT, meta);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIGHT);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(this);
    }
}
