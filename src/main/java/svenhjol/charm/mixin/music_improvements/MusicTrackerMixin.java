package svenhjol.charm.mixin.music_improvements;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.music_improvements.MusicImprovementsClient;

@Mixin(MusicManager.class)
public class MusicTrackerMixin {
    @Shadow private SoundInstance current;

    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookTick(CallbackInfo ci) {
        if (MusicImprovementsClient.isEnabled && MusicImprovementsClient.handleTick(this.current))
            ci.cancel();
    }

    @Inject(
        method = "stop",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookStop(CallbackInfo ci) {
        if (MusicImprovementsClient.isEnabled && MusicImprovementsClient.handleStop())
            ci.cancel();
    }

    @Inject(
        method = "isPlayingType",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsPlayingType(Music music, CallbackInfoReturnable<Boolean> cir) {
        if (MusicImprovementsClient.isEnabled && MusicImprovementsClient.handlePlaying(music))
            cir.setReturnValue(true);
    }
}
