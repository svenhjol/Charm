package svenhjol.meson.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.event.PlayerTickCallback;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((PlayerEntity)(Object)this);
    }
}
