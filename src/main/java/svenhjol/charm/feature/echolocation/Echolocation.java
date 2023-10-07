package svenhjol.charm.feature.echolocation;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.event.PlayerTickEvent;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "A status effect that causes all living entities around the player to glow.")
public class Echolocation extends CharmonyFeature {
    private static final String ID = "echolocation";
    private static final int CHECK_TICKS = 10;
    private static final int GLOW_RANGE = 24;
    public static Supplier<EcholocationStatusEffect> MOB_EFFECT;

    @Override
    public void register() {
        MOB_EFFECT = Charm.instance().registry().mobEffect(ID, EcholocationStatusEffect::new);
    }

    @Override
    public void runWhenEnabled() {
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide()
            && player.level().getGameTime() % CHECK_TICKS == 0
            && player.hasEffect(MOB_EFFECT.get())
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
