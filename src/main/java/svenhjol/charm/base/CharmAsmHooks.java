package svenhjol.charm.base;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import svenhjol.charm.decoration.feature.AllTheBarrels;
import svenhjol.charm.tweaks.feature.RemovePotionGlint;
import svenhjol.charm.tweaks.feature.StackablePotions;

public class CharmAsmHooks
{
    public static boolean removePotionGlint()
    {
        return CharmLoader.hasFeature(RemovePotionGlint.class);
    }

    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return CharmLoader.hasFeature(StackablePotions.class);
    }

    public static boolean isBarrel(Block block)
    {
        return CharmLoader.hasFeature(AllTheBarrels.class)
            && (block == Blocks.BARREL || block instanceof BarrelBlock);
    }
}
