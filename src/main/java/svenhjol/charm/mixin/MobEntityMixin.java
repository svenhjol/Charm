package svenhjol.charm.mixin;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.module.MobSoundCulling;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Shadow public int ambientSoundChance;

    @Inject(
        method = "resetSoundDelay",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetMinAmbientSoundDelay(CallbackInfo ci) {
        if (ModuleHandler.enabled(MobSoundCulling.class)) {
            this.ambientSoundChance = -MobSoundCulling.getMinAmbientSoundDelay((MobEntity) (Object) this);
            ci.cancel();
        }
    }
}
