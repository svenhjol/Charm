package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.automation.feature.VariableRedstoneLamp;
import svenhjol.meson.iface.IMesonBlock;

import java.util.Random;

public class BlockVariableRedstoneLight extends BlockRedstoneLight implements IMesonBlock
{
    public static PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    public BlockVariableRedstoneLight()
    {
        super(false);
        register();
        setCreativeTab(CreativeTabs.REDSTONE);
        setSoundType(SoundType.GLASS);
        setHardness(0.3F);
        setLightLevel(1.0F);
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
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote) {
            if (state.getValue(LEVEL) > 0 && !worldIn.isBlockPowered(pos)) {
                updateState(worldIn, pos, state, 0);
            } else if (worldIn.isBlockPowered(pos)) {
                int power = worldIn.getRedstonePowerFromNeighbors(pos);
                updateState(worldIn, pos, state, power);
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote) {
            if (state.getValue(LEVEL) > 0 && !worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
            } else if (worldIn.isBlockPowered(pos)) {
                int power = worldIn.isBlockPowered(pos) ? worldIn.getRedstonePowerFromNeighbors(pos) : 0;
                updateState(worldIn, pos, state, power);
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int power;

        if (!worldIn.isRemote && state.getValue(LEVEL) > 0) {
            power = worldIn.isBlockPowered(pos) ? worldIn.getRedstonePowerFromNeighbors(pos) : 0;
            updateState(worldIn, pos, state, power);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LEVEL);
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

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(VariableRedstoneLamp.block);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(this);
    }

    private void updateState(World world, BlockPos pos, IBlockState state, int power)
    {
        world.setBlockState(pos, state.withProperty(LEVEL, power), 2);
        world.checkLight(pos);
    }
}
