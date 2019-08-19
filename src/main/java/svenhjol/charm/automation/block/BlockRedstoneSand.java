package svenhjol.charm.automation.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import svenhjol.charm.Charm;
import svenhjol.charm.automation.feature.RedstoneSand;
import svenhjol.meson.iface.IMesonBlock;

public class BlockRedstoneSand extends BlockFalling implements IMesonBlock
{
    protected String name;

    public BlockRedstoneSand()
    {
        name = "redstone_sand";
        register(name);
        setSoundType(SoundType.SAND);
        setHardness(RedstoneSand.hardness);
        setCreativeTab(CreativeTabs.REDSTONE);
        setHarvestLevel("shovel", 0);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
