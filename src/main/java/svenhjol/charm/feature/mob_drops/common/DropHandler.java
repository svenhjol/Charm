package svenhjol.charm.feature.mob_drops.common;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface DropHandler {
    default Optional<ItemStack> dropWhenKilled(LivingEntity entity, DamageSource source) {
        return Optional.empty();
    }

    default Optional<ItemStack> dropSometimes(LivingEntity entity) {
        return Optional.empty();
    }
}
