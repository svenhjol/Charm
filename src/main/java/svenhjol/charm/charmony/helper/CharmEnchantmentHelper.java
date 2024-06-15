package svenhjol.charm.charmony.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;
import java.util.Optional;

public class CharmEnchantmentHelper {
    public CharmEnchantmentHelper() {
    }

    public static void applyOnItem(ItemStack stack, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.put(enchantment, level);
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    public static int getEntityFortuneLevel(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, entity);
    }

    public static boolean entityHasFeatherFalling(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, entity) > 0;
    }

    public static boolean itemHasEnchantment(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return enchantments.containsKey(enchantment);
    }

    public static boolean itemHasEnchantment(ItemStack stack, ResourceLocation enchantment) {
        Optional<Enchantment> opt = BuiltInRegistries.ENCHANTMENT.getOptional(enchantment);
        return opt.isPresent() && itemHasEnchantment(stack, opt.get());
    }
}
