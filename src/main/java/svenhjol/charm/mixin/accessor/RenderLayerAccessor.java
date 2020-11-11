package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderLayer.class)
public interface RenderLayerAccessor {
    @Accessor("ARMOR_GLINT")
    static RenderLayer getArmorGlint() {
        throw new IllegalStateException();
    }

    @Accessor("ARMOR_ENTITY_GLINT")
    static RenderLayer getArmorEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("DIRECT_GLINT")
    static RenderLayer getDirectGlint() {
        throw new IllegalStateException();
    }

    @Accessor("DIRECT_ENTITY_GLINT")
    static RenderLayer getDirectEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("ENTITY_GLINT")
    static RenderLayer getEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT")
    static RenderLayer getGlint() {
        throw new IllegalStateException();
    }
}
