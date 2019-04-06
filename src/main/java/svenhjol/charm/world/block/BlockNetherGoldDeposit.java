package svenhjol.charm.world.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonBlock;
import svenhjol.charm.world.feature.NetherGoldDeposits;

import java.util.Random;

public class BlockNetherGoldDeposit extends MesonBlock
{
    public BlockNetherGoldDeposit()
    {
        super(Material.ROCK, "nether_gold_deposit");
        setHardness(NetherGoldDeposits.hardness);
        setResistance(NetherGoldDeposits.resistance);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.GOLD_NUGGET;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        if (fortune > 0) {
			int i = random.nextInt(fortune + 2) - 1;
			if (i < 0) i = 0;
			return this.quantityDropped(random) * (i + 1);
		}
		return this.quantityDropped(random);
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, NetherGoldDeposits.minNuggets, NetherGoldDeposits.maxNuggets);
    }
}