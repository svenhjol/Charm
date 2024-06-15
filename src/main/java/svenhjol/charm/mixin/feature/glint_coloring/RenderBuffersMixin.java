package svenhjol.charm.mixin.feature.glint_coloring;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;

import java.util.SortedMap;

@Mixin(RenderBuffers.class)
public class RenderBuffersMixin {

    @Final
    @Shadow
    public SortedMap<RenderType, BufferBuilder> fixedBuffers;
    /**
     * Capture a reference to the buffer builders so we can add new elements to it.
     */
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void hookInit(CallbackInfo ci) {
        Resolve.feature(GlintColoringClient.class).handlers.setBuilders(fixedBuffers);
    }
}
