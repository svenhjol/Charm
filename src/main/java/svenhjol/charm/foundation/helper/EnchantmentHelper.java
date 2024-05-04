package svenhjol.charm.foundation.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;

public final class EnchantmentHelper {
    public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment) {
        if (stack.has(DataComponents.ENCHANTMENTS)) {
            var enchantments = stack.get(DataComponents.ENCHANTMENTS);
            if (enchantments == null) return false;

            for (Holder<Enchantment> holder : enchantments.keySet()) {
                if (holder.value() == enchantment) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsSameEnchantments(ItemEnchantments aa, ItemEnchantments bb) {
        List<Enchantment> pool = new ArrayList<>();

        if (aa != null) {
            for (Holder<Enchantment> holder : aa.keySet()) {
                pool.add(holder.value());
            }
        }

        if (bb != null) {
            for (Holder<Enchantment> holder : bb.keySet()) {
                if (!pool.contains(holder.value())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean containsSameEnchantments(ItemStack stack, ItemEnchantments enchantments) {
        var fromStack = stack.get(DataComponents.ENCHANTMENTS);

        if (fromStack != null) {
            return containsSameEnchantments(fromStack, enchantments);
        }

        return enchantments.isEmpty();
    }

    public static boolean containsSameEnchantments(ItemStack a, ItemStack b) {
        var aa = a.get(DataComponents.ENCHANTMENTS);
        var bb = b.get(DataComponents.ENCHANTMENTS);

        if (aa == null && bb == null) return true;

        return containsSameEnchantments(aa, bb);
    }
}
