package svenhjol.charm.mixin.strays_spawn_below_surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.strays_spawn_below_surface.StraysSpawnBelowSurface;

import java.util.Random;

@Mixin(Stray.class)
public abstract class AllowSpawningAnywhereMixin {
    /**
     * Defer to canSpawn. If the check is false, use the default stray spawn logic.
     */
    @Inject(
        method = "checkStraySpawnRules",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookCanSpawn(EntityType<Stray> entity, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && StraysSpawnBelowSurface.canSpawn()) {
            cir.setReturnValue(Stray.checkMonsterSpawnRules(entity, level, reason, pos, rand));
        }
    }
}
