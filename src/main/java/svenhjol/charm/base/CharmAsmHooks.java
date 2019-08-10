package svenhjol.charm.base;

import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.feature.RemovePotionGlint;
import svenhjol.charm.tweaks.feature.StackablePotions;

public class CharmAsmHooks
{
    public static boolean removePotionGlint()
    {
        return Charm.hasFeature(RemovePotionGlint.class);
    }

    public static boolean checkBrewingStandStack(ItemStack stack)
    {
        return Charm.hasFeature(StackablePotions.class);
    }
}
