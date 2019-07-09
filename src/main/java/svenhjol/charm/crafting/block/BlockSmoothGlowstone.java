package svenhjol.charm.crafting.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.SmoothGlowstone;
import svenhjol.meson.MesonBlock;

import java.util.Random;

public class BlockSmoothGlowstone extends MesonBlock
{
    public BlockSmoothGlowstone()
    {
        super(Material.GLASS, "smooth_glowstone");
        setLightLevel(1.0f);
        setSoundType(SoundType.GLASS);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(SmoothGlowstone.hardness);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return MathHelper.clamp(quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
    }

    public int quantityDropped(Random random) {
        return 2 + random.nextInt(3);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.GLOWSTONE_DUST;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(this, 1);
    }
}
