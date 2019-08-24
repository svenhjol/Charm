package svenhjol.charm.smithing.compat;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.smithing.module.TallowIncreasesDurability;

import java.util.Random;

public class QuarkTallow
{
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;

        Item l = left.getItem();
        Item r = right.getItem();

        Item tallow = ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark:tallow"));
        if (r != tallow) return;

        boolean repairable = l instanceof ToolItem
            || l instanceof ArmorItem
            || TallowIncreasesDurability.repairable.contains(l.getClass());
        if (!repairable) return;

        int currentDamage = l.getDamage(left);
        int maxDamage = l.getMaxDamage();

        if (currentDamage == 0) return;

        out = left.copy();
        out.setRepairCost(left.getRepairCost() + (new Random().nextDouble() < TallowIncreasesDurability.chanceOfCost ? 1 : 0));
        out.setDamage(currentDamage - (int)(maxDamage * TallowIncreasesDurability.amountRepaired));

        event.setOutput(out);
        event.setCost(TallowIncreasesDurability.xpCost);
        event.setMaterialCost(1);
    }
}
