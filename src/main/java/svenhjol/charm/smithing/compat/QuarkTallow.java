package svenhjol.charm.smithing.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.AnvilUpdateEvent;
import svenhjol.charm.smithing.feature.TallowIncreasesDurability;
import vazkii.quark.decoration.item.ItemTallow;

import java.util.Random;

public class QuarkTallow
{
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (in.isEmpty() || combine.isEmpty()) return;

        Item i = in.getItem();
        Item c = combine.getItem();

        boolean repairable = i instanceof ItemTool
            || i instanceof ItemArmor
            || TallowIncreasesDurability.repairable.contains(i.getClass());

        if (repairable && c instanceof ItemTallow) {

            // get the current damage
            int currentDamage = in.getItemDamage();
            int maxDamage = in.getMaxDamage();

            if (currentDamage == 0) {
                event.setCanceled(true);
                return;
            }

            ItemStack out = in.copy();

            out.setRepairCost(in.getRepairCost() + (new Random().nextFloat() < 0.75f ? 1 : 0));
            out.setItemDamage(currentDamage - (int)(maxDamage * 0.02f));

            event.setOutput(out);
            event.setCost(TallowIncreasesDurability.xpCost);
            event.setMaterialCost(1);
        }
    }
}
