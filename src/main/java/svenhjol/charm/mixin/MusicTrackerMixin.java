package svenhjol.charm.mixin;

import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.client.MusicClient;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
    @Shadow private SoundInstance current;

    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tickHook(CallbackInfo ci) {
        if (MusicClient.enabled && MusicClient.handleTick(this.current))
            ci.cancel();
    }

    @Inject(
        method = "stop",
        at = @At("HEAD"),
        cancellable = true
    )
    private void stopHook(CallbackInfo ci) {
        if (MusicClient.enabled && MusicClient.handleStop())
            ci.cancel();
    }

    @Inject(
        method = "isPlayingType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isPlayingHook(MusicSound music, CallbackInfoReturnable<Boolean> cir) {
        if (MusicClient.enabled && MusicClient.handlePlaying(music))
            cir.setReturnValue(true);
    }
}
