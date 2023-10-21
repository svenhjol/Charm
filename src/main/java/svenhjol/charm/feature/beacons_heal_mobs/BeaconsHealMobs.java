package svenhjol.charm.feature.beacons_heal_mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.event.ApplyBeaconEffectsEvent;

public class BeaconsHealMobs extends CommonFeature {
    @Override
    public String description() {
        return "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.";
    }

    @Override
    public void runWhenEnabled() {
        ApplyBeaconEffectsEvent.INSTANCE.handle(this::handleApplyBeaconEffects);
    }

    private void handleApplyBeaconEffects(Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary) {
        if (!level.isClientSide()) {
            var d0 = levels * 10.0d + 10.0d;
            var bb = (new AABB(pos)).inflate(d0).inflate(0.0d, level.getMaxBuildHeight(), 0.0d);

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
            player -> Advancements.trigger(new ResourceLocation(Charm.ID, "healed_near_beacon"), player));
    }
}
