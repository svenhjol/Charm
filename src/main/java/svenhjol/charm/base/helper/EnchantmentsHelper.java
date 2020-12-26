package svenhjol.charm.base.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantmentsHelper {
    /**
     * Add blocks to this list that should provide enchanting power to an enchanting table.
     */
    public static List<Block> ENCHANTING_BLOCKS = new ArrayList<>();

    public static boolean canBlockPowerEnchantingTable(BlockState state) {
        boolean result = ENCHANTING_BLOCKS.stream().anyMatch(b -> b == state.getBlock());
        return result;
    }

    public static void apply(ItemStack stack, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        enchantments.put(enchantment, level);
        EnchantmentHelper.set(enchantments, stack);
    }

    public static boolean hasFeatherFalling(LivingEntity entity) {
        return EnchantmentHelper.getEquipmentLevel(Enchantments.FEATHER_FALLING, entity) > 0;
    }

    public static boolean has(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        return enchantments.containsKey(enchantment);
    }
}
