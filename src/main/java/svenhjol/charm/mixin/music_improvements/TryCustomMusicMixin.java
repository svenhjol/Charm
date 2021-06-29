package svenhjol.charm.mixin.music_improvements;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.module.music_improvements.MusicImprovementsClient;

@Mixin(Minecraft.class)
@CharmMixin(disableIfModsPresent = {"charmonium"})
public class TryCustomMusicMixin {
    @Inject(
        method = "getSituationalMusic",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome;"
        ),
        cancellable = true
    )
    private void hookSituationalMusic(CallbackInfoReturnable<Music> cir) {
        if (MusicImprovementsClient.isEnabled) {
            Music music = MusicImprovementsClient.getMusic();
            if (music != null)
                cir.setReturnValue(music);
        }
    }
}
