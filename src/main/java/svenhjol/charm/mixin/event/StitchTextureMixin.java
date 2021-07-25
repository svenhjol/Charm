package svenhjol.charm.mixin.event;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.StitchTextureCallback;

import java.util.Set;
import java.util.stream.Stream;

@Mixin(TextureAtlas.class)
public class StitchTextureMixin {
    /**
     * Fires the {@link StitchTextureCallback} event.
     *
     * Simulates Forge's TextureStitchEvent.Pre
     *
     * This is required for variant chest textures but can be used
     * by other modules for adding custom textures to the sprite atlas.
     */
    @Inject(
        method = "prepareToStitch",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookStitch(ResourceManager resourceManager, Stream<ResourceLocation> idStream, ProfilerFiller profiler, int mipmapLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, Set<ResourceLocation> set) {
        StitchTextureCallback.EVENT.invoker().interact((TextureAtlas)(Object)this, set);
    }
}
