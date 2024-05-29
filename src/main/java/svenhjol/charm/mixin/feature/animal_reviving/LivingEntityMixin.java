package svenhjol.charm.mixin.feature.animal_reviving;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;
import svenhjol.charm.charmony.Resolve;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
        method = "shouldDropLoot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookShouldDropLoot(CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof OwnableEntity ownable
            && ownable.getOwnerUUID() != null) {
            cir.setReturnValue(Resolve.feature(AnimalReviving.class).dropLootOnDeath());
        }
    }

    @Inject(
        method = "shouldDropExperience",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookShouldDropExperience(CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof OwnableEntity ownable
            && ownable.getOwnerUUID() != null) {
            cir.setReturnValue(Resolve.feature(AnimalReviving.class).dropExperienceOnDeath());
        }
    }
}
