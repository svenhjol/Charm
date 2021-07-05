package svenhjol.charm.mixin.potion_of_hogsbane;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.potion_of_hogsbane.PotionOfHogsbane;

@Mixin(Hoglin.class)
public class PreventAttackingHoglinMixin {
    @Inject(
        method = {"doHurtTarget"},
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookBeforeAttack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity && PotionOfHogsbane.hasHogsbaneEffect((LivingEntity) entity))
            cir.setReturnValue(false);
    }
}
