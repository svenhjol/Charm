package svenhjol.charm.feature.echolocation;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<Echolocation> {
    static final String ID = "echolocation";
    static final int CHECK_TICKS = 10;
    static final int GLOW_RANGE = 24;

    public CommonRegistration(Echolocation feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Echolocation.mobEffect = feature.registry().mobEffect(ID, EcholocationStatusEffect::new);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide()
            && player.level().getGameTime() % CHECK_TICKS == 0
            && player.hasEffect(Echolocation.mobEffect.get())
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
