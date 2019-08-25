package svenhjol.charm.base;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.module.LeatherArmorInvisibility;
import svenhjol.charm.tweaks.module.NoAnvilMinimumXp;
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

    public static int getMinimumRepairCost()
    {
        return Charm.loader.hasModule(NoAnvilMinimumXp.class) ? -1 : 0;
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack)
    {
        if (!Charm.loader.hasModule(LeatherArmorInvisibility.class)) return false;
        return LeatherArmorInvisibility.isArmorInvisible(entity, stack);
    }
}
