package svenhjol.charm.charmony.common.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.Nullable;

public final class EnchantmentsHelper {
    public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment) {
        var enchantments = EnchantmentHelper.getEnchantments(stack);
        return enchantments.containsKey(enchantment);
    }

    public static int featherFallingLevel(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, entity);
    }

    public static int lootingLevel(DamageSource source) {
        return source.getDirectEntity() instanceof LivingEntity entity ? lootingLevel(entity) : 0;
    }

    public static int lootingLevel(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.MOB_LOOTING, entity);
    }

    @Nullable
    public static Holder<Enchantment> holder(RegistryAccess registryAccess, ResourceKey<Enchantment> key) {
        var registry = registryAccess.registry(Registries.ENCHANTMENT).orElse(null);
        if (registry == null) return null;

        var resolved = registry.getHolder(key).orElse(null);
        if (resolved == null) return null;

        return resolved;
    }
}
