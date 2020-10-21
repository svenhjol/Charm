package svenhjol.charm.mixin;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.TextureStitchCallback;

import java.util.Set;
import java.util.stream.Stream;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
    /**
     * Simulates Forge's TextureStitchEvent.Pre
     * This is required for variant chest textures.
     */
    @Inject(
        method = "stitch",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookStitch(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler, int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir, Set<Identifier> set) {
        TextureStitchCallback.EVENT.invoker().interact((SpriteAtlasTexture)(Object)this, set);
    }
}
