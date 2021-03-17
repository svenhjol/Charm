package svenhjol.charm.base.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;

public class AnvilHelper {
    public static ItemStack increaseRepairCost(ItemStack out) {
        int cost = out.getRepairCost();
        int o = Math.min(out.getDamage(), out.getMaxDamage() / 4);
        if (o <= 0)
            return ItemStack.EMPTY;

        out.setDamage(out.getDamage() - o);
        out.setRepairCost(AnvilScreenHandler.getNextCost(cost));
        return out;
    }
}
