package svenhjol.charm.mixin.accessor;

import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(RenderStateShard.class)
@CharmMixin(required = true)
public interface RenderStateShardAccessor {
    @Accessor("COLOR_WRITE")
    static RenderStateShard.WriteMaskStateShard getColorWrite() {
        throw new IllegalStateException();
    }

    @Accessor("NO_CULL")
    static RenderStateShard.CullStateShard getNoCull() {
        throw new IllegalStateException();
    }

    @Accessor("EQUAL_DEPTH_TEST")
    static RenderStateShard.DepthTestStateShard getEqualDepthTest() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getGlintTransparency() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT_TEXTURING")
    static RenderStateShard.TexturingStateShard getGlintTexturing() {
        throw new IllegalStateException();
    }

    @Accessor("ENTITY_GLINT_TEXTURING")
    static RenderStateShard.TexturingStateShard getEntityGlintTexturing() {
        throw new IllegalStateException();
    }

    @Accessor("ITEM_ENTITY_TARGET")
    static RenderStateShard.OutputStateShard getItemEntityTarget() {
        throw new IllegalStateException();
    }

    @Accessor("VIEW_OFFSET_Z_LAYERING")
    static RenderStateShard.LayeringStateShard getViewOffsetZLayering() {
        throw new IllegalStateException();
    }

    @Accessor("RENDERTYPE_GLINT_SHADER")
    static RenderStateShard.ShaderStateShard getGlintShader() { throw new IllegalStateException(); }

    @Accessor("RENDERTYPE_ENTITY_GLINT_SHADER")
    static RenderStateShard.ShaderStateShard getEntityGlintShader() { throw new IllegalStateException(); }

    @Accessor("RENDERTYPE_ARMOR_GLINT_SHADER")
    static RenderStateShard.ShaderStateShard getArmorGlintShader() { throw new IllegalStateException(); }

    @Accessor("RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER")
    static RenderStateShard.ShaderStateShard getArmorEntityGlintShader() { throw new IllegalStateException(); }

    @Accessor("RENDERTYPE_GLINT_DIRECT_SHADER")
    static RenderStateShard.ShaderStateShard getGlintDirectShader() { throw new IllegalStateException(); }

    @Accessor("RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER")
    static RenderStateShard.ShaderStateShard getEntityGlintDirectShader() { throw new IllegalStateException(); }

}
