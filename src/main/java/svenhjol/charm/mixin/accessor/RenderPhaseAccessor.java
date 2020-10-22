package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {
    @Accessor("COLOR_MASK")
    static RenderPhase.WriteMaskState getColorMask() {
        throw new IllegalStateException();
    }

    @Accessor("DISABLE_CULLING")
    static RenderPhase.Cull getDisableCulling() {
        throw new IllegalStateException();
    }

    @Accessor("EQUAL_DEPTH_TEST")
    static RenderPhase.DepthTest getEqualDepthTest() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT_TRANSPARENCY")
    static RenderPhase.Transparency getGlintTransparency() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT_TEXTURING")
    static RenderPhase.Texturing getGlintTexturing() {
        throw new IllegalStateException();
    }
}
