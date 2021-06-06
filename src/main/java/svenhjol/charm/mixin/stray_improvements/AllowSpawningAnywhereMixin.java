package svenhjol.charm.mixin.stray_improvements;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.stray_improvements.StrayImprovements;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Stray.class)
public abstract class AllowSpawningAnywhereMixin {

    /**
     * Defer to canSpawn. If the check is false, use the default stray spawn logic.
     */
    @Inject(
        method = "canSpawn",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookCanSpawn(EntityType<Stray> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && StrayImprovements.canSpawn())
            cir.setReturnValue(Stray.checkMonsterSpawnRules(entity, world, reason, pos, rand));
    }
}
