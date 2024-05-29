package svenhjol.charm.feature.echolocation.common;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<Echolocation> {
    private static final int CHECK_TICKS = 10;
    private static final int GLOW_RANGE = 24;

    public Handlers(Echolocation feature) {
        super(feature);
    }

    public void playerTick(Player player) {
        if (!player.level().isClientSide()
            && player.level().getGameTime() % CHECK_TICKS == 0
            && player.hasEffect(feature().registers.mobEffect.get())
        ) {
            var box = player.getBoundingBox().inflate(GLOW_RANGE, GLOW_RANGE / 2.0, GLOW_RANGE);
            var effect = new MobEffectInstance(MobEffects.GLOWING, CHECK_TICKS);
            var entities = player.level().getEntitiesOfClass(LivingEntity.class, box);

            for (LivingEntity entity : entities) {
                if (entity.getUUID().equals(player.getUUID())) continue;
                if (entity.canBeAffected(effect)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, CHECK_TICKS + 5, 0, false, true), player);
                }
            }
        }
    }
}
