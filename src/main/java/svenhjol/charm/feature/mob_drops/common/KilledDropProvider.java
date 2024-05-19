package svenhjol.charm.feature.mob_drops.common;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface KilledDropProvider<T extends LivingEntity> {
    ItemStack stackWhenKilled(T entity, DamageSource source);
}
