package svenhjol.charm.feature.beacons_heal_mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.event.ApplyBeaconEffectsEvent;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.")
public class BeaconsHealMobs extends CharmFeature {
    @Override
    public void runWhenEnabled() {
        ApplyBeaconEffectsEvent.INSTANCE.handle(this::handleApplyBeaconEffects);
    }

    private void handleApplyBeaconEffects(Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary) {
        if (!level.isClientSide()) {
            var d0 = levels * 10.0D + 10.0D;
            var bb = (new AABB(pos)).inflate(d0).inflate(0.0D, level.getMaxBuildHeight(), 0.0D);

            if (primary == MobEffects.REGENERATION || secondary == MobEffects.REGENERATION) {
                var list = level.getEntitiesOfClass(AgeableMob.class, bb);
                list.forEach(mob -> mob.addEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 4 * 20, 1)));
            }
        }
    }
}
