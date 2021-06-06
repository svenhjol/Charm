package svenhjol.charm.mixin.callback;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlaySoundCallback;

@Mixin(SoundEngine.class)
public class PlaySoundCallbackMixin {
    @Inject(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/SoundLoader;loadStatic(Lnet/minecraft/util/Identifier;)Ljava/util/concurrent/CompletableFuture;"
        )
    )
    private void hookPlayStatic(SoundInstance soundInstance, CallbackInfo ci) {
        PlaySoundCallback.EVENT.invoker().interact((SoundEngine)(Object)this, soundInstance);
    }

    @Inject(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/SoundLoader;loadStreamed(Lnet/minecraft/util/Identifier;Z)Ljava/util/concurrent/CompletableFuture;"
        )
    )
    private void hookPlayStreamed(SoundInstance soundInstance, CallbackInfo ci) {
        PlaySoundCallback.EVENT.invoker().interact((SoundEngine)(Object)this, soundInstance);
    }
}
