package svenhjol.charm.charmony.common.mixin.event.apply_beacon_effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.event.ApplyBeaconEffectsEvent;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    /**
     * Fires the {@link ApplyBeaconEffectsEvent} event when adding vanilla beacon effects.
     * This allows Charm modules to add additional effects to entities in range.
     */
    @Inject(
        method = "applyEffects",
        at = @At("HEAD")
    )
    private static void hookApplyEffects(Level level, BlockPos pos, int beaconLevel, MobEffect primary, MobEffect secondary, CallbackInfo ci) {
        if (level != null) {
            ApplyBeaconEffectsEvent.INSTANCE.invoke(level, pos, beaconLevel, primary, secondary);
        }
    }
}
