package svenhjol.charm.helper;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class AnvilHelper {
    public static ItemStack increaseRepairCost(ItemStack out) {
        int cost = out.getBaseRepairCost();
        int o = Math.min(out.getDamageValue(), out.getMaxDamage() / 4);
        if (o <= 0)
            return ItemStack.EMPTY;

        out.setDamageValue(out.getDamageValue() - o);
        out.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(cost));
        return out;
    }
}
