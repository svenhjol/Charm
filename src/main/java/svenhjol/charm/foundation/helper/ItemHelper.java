package svenhjol.charm.foundation.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class ItemHelper {
    public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment) {
        if (stack.has(DataComponents.ENCHANTMENTS)) {
            var enchantments = stack.get(DataComponents.ENCHANTMENTS);
            for (Holder<Enchantment> holder : enchantments.keySet()) {
                if (holder.value() == enchantment) {
                    return true;
                }
            }
        }
        return false;
    }
}
