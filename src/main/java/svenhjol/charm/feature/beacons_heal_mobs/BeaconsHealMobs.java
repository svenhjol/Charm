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
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.event.ApplyBeaconEffectsEvent;

@Feature(mod = Charm.MOD_ID, description = "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.")
public class BeaconsHealMobs extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        ApplyBeaconEffectsEvent.INSTANCE.handle(this::handleApplyBeaconEffects);
    }

    private void handleApplyBeaconEffects(Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary) {
        if (!level.isClientSide()) {
            var d0 = levels * 10.0D + 10.0D;
            var bb = (new AABB(pos)).inflate(d0).inflate(0.0D, level.getMaxBuildHeight(), 0.0D);

            if (primary == MobEffects.REGENERATION || secondary == MobEffects.REGENERATION) {
                var mobs = level.getEntitiesOfClass(AgeableMob.class, bb);
                if (!mobs.isEmpty()) {
                    triggerHealedNearBeacon(level, pos);
                }
                mobs.forEach(mob -> mob.addEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 4 * 20, 1)));
            }
        }
    }

    public static void triggerHealedNearBeacon(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(Charm.instance().makeId("healed_near_beacon"), player));
    }
}
