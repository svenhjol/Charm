package svenhjol.charm.mixin.husk_improvements;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.husk_improvements.HuskImprovements;

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
    private static void hookCanSpawn(EntityType<Husk> entity, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && HuskImprovements.canSpawn())
            cir.setReturnValue(Husk.checkMonsterSpawnRules(entity, world, reason, pos, rand));
    }
}
