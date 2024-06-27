package svenhjol.charm.feature.beacons_heal_mobs.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<BeaconsHealMobs> {
    public Handlers(BeaconsHealMobs feature) {
        super(feature);
    }

    public void applyBeaconEffects(Level level, BlockPos pos, int levels,
                                   @Nullable Holder<MobEffect> primary, @Nullable Holder<MobEffect> secondary) {
        if (primary == null || secondary == null) {
            return;
        }

        if (!level.isClientSide()) {
            var d0 = levels * 10.0d + 10.0d;
            var bb = (new AABB(pos)).inflate(d0).inflate(0.0d, level.getMaxBuildHeight(), 0.0d);

            if (primary.equals(MobEffects.REGENERATION) || secondary.equals(MobEffects.REGENERATION)) {
                var mobs = level.getEntitiesOfClass(AgeableMob.class, bb);
                if (!mobs.isEmpty()) {
                    feature().advancements.healedNearBeacon(level, pos);
                }
                mobs.forEach(mob -> mob.addEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 4 * 20, 1)));
            }
        }
    }
}
