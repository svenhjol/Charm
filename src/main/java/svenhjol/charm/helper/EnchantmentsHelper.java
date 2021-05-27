package svenhjol.charm.helper;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import svenhjol.charm.init.CharmTags;

import java.util.Map;

public class EnchantmentsHelper {
    public static boolean canBlockPowerEnchantingTable(BlockState state) {
        return state.isIn(CharmTags.PROVIDE_ENCHANTING_POWER);
    }

    public static void apply(ItemStack stack, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        enchantments.put(enchantment, level);
        EnchantmentHelper.set(enchantments, stack);
    }

    public static int getFortune(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(Enchantments.FORTUNE, entity);
    }

    public static boolean hasFeatherFalling(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(Enchantments.FEATHER_FALLING, entity) > 0;
    }

    public static boolean has(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        return enchantments.containsKey(enchantment);
    }
}
