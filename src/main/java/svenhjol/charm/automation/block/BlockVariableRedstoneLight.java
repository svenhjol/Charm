package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonBlock;

import java.util.Random;

public class BlockVariableRedstoneLight extends BlockRedstoneLight implements IMesonBlock
{
    public static PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    public BlockVariableRedstoneLight()
    {
        super(false);
        register();
        setLightLevel(1.0f);
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
        worldIn.scheduleUpdate(pos, this, 4);
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
        return state.getValue(LEVEL);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int power;

        if (!worldIn.isRemote) {
            power = worldIn.isBlockPowered(pos) ? worldIn.getRedstonePowerFromNeighbors(pos) : 0;
            updateLight(worldIn, pos, state, power);
        }
    }

    private void updateLight(World world, BlockPos pos, IBlockState state, int power)
    {
        world.setBlockState(pos, state.withProperty(LEVEL, power), 3);
        world.checkLight(pos);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LEVEL, meta);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LEVEL);
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
