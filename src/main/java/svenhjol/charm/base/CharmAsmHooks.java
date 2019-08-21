package svenhjol.charm.base;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.decoration.module.AllTheBarrels;
import svenhjol.charm.tweaks.module.RemovePotionGlint;
import svenhjol.charm.tweaks.module.StackablePotions;

public class CharmAsmHooks
{
    public static boolean removePotionGlint()
    {
        return Charm.loader.hasModule(RemovePotionGlint.class);
    }

    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Charm.loader.hasModule(StackablePotions.class);
    }

    public static boolean isBarrel(Block block)
    {
        return Charm.loader.hasModule(AllTheBarrels.class)
            && (block == Blocks.BARREL || block instanceof BarrelBlock);
    }
}
