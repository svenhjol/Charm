package svenhjol.charm.feature.mob_drops.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ChanceDropProvider<T extends LivingEntity> {
    ItemStack stackByChance(T entity);
}
