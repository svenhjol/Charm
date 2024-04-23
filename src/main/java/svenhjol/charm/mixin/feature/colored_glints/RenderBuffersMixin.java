package svenhjol.charm.mixin.feature.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.feature.colored_glints.ClientCallbacks;

import java.util.SortedMap;

@Mixin(RenderBuffers.class)
public class RenderBuffersMixin {
    /**
     * Capture a reference to the buffer builders so we can add new elements to it.
     */
    @Inject(
        method = "<init>",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookInit(int i, CallbackInfo ci, SortedMap<RenderType, BufferBuilder> builders) {
        ClientCallbacks.builders = builders;
    }
}
