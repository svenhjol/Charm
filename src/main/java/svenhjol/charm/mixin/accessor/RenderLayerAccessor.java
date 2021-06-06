package svenhjol.charm.mixin.accessor;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(RenderType.class)
@CharmMixin(required = true)
public interface RenderLayerAccessor {
    @Accessor("ARMOR_GLINT")
    static RenderType getArmorGlint() {
        throw new IllegalStateException();
    }

    @Accessor("ARMOR_ENTITY_GLINT")
    static RenderType getArmorEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("DIRECT_GLINT")
    static RenderType getDirectGlint() {
        throw new IllegalStateException();
    }

    @Accessor("DIRECT_ENTITY_GLINT")
    static RenderType getDirectEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("ENTITY_GLINT")
    static RenderType getEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT")
    static RenderType getGlint() {
        throw new IllegalStateException();
    }
}
