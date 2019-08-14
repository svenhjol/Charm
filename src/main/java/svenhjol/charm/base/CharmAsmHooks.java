package svenhjol.charm.base;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.decoration.feature.AllTheBarrels;
import svenhjol.charm.tweaks.feature.RemovePotionGlint;
import svenhjol.charm.tweaks.feature.StackablePotions;

public class CharmAsmHooks
{
    public static boolean removePotionGlint()
    {
        return Charm.loader.hasFeature(RemovePotionGlint.class);
    }

    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Charm.loader.hasFeature(StackablePotions.class);
    }

    public static boolean isBarrel(Block block)
    {
        return Charm.loader.hasFeature(AllTheBarrels.class)
            && (block == Blocks.BARREL || block instanceof BarrelBlock);
    }
}
