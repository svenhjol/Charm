package svenhjol.charm.mixin.husks_spawn_below_surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.husks_spawn_below_surface.HusksSpawnBelowSurface;

import java.util.Random;

@Mixin(Husk.class)
public abstract class AllowSpawningAnywhereMixin {
    /**
     * Defer to canSpawn. If the check is false, use the default husk spawn logic.
     */
    @Inject(
        method = "checkHuskSpawnRules",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookCanSpawn(EntityType<Husk> entity, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && HusksSpawnBelowSurface.canSpawn()) {
            cir.setReturnValue(Husk.checkMonsterSpawnRules(entity, level, reason, pos, rand));
        }
    }
}
