package svenhjol.charm.feature.mob_drops.common;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.mob_drops.MobDrops;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<MobDrops> {
    public Handlers(MobDrops feature) {
        super(feature);
    }

    public void entityTick(LivingEntity entity) {
        if (entity.level().isClientSide()) return;

        for (var handler : feature().registers.dropHandlers) {
            var stack = handler.dropSometimes(entity).orElse(ItemStack.EMPTY);

            if (!stack.isEmpty()) {
                spawnEntityItem(entity, stack);
            }
        }
    }

    public InteractionResult entityKilledDrop(LivingEntity entity, DamageSource source) {
        if (!entity.level().isClientSide()) {
            for (var handler : feature().registers.dropHandlers) {
                var stack = handler.dropWhenKilled(entity, source).orElse(ItemStack.EMPTY);

                if (!stack.isEmpty()) {
                    spawnEntityItem(entity, stack);
                }
            }
        }

        return InteractionResult.PASS;
    }

    public void spawnEntityItem(LivingEntity entity, ItemStack stack) {
        var level = entity.level();
        var pos = entity.blockPosition();
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));
    }
}
