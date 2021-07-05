package svenhjol.charm.mixin.potion_of_hogsbane;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.potion_of_hogsbane.PotionOfHogsbane;

@Mixin(HoglinAi.class)
public class PreventAttackingAiMixin {
    @Inject(
        method = {"maybeRetaliate", "setAttackTarget", "broadcastAttackTarget", "setAttackTargetIfCloserThanCurrent"},
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookBeforeAttack(Hoglin hoglin, LivingEntity livingEntity, CallbackInfo ci) {
        if (PotionOfHogsbane.hasHogsbaneEffect(livingEntity))
            ci.cancel();
    }
}
