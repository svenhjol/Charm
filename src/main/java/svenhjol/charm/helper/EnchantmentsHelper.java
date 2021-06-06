package svenhjol.charm.helper;

import svenhjol.charm.init.CharmTags;

import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantmentsHelper {
    public static boolean canBlockPowerEnchantingTable(BlockState state) {
        return state.is(CharmTags.PROVIDE_ENCHANTING_POWER);
    }

    public static void apply(ItemStack stack, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.put(enchantment, level);
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    public static int getFortune(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, entity);
    }

    public static boolean hasFeatherFalling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, entity) > 0;
    }

    public static boolean has(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return enchantments.containsKey(enchantment);
    }
}
