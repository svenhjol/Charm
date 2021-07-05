package svenhjol.charm.mixin.accessor;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
    @Accessor("ARMOR_GLINT")
    static RenderType getArmorGlint() {
        throw new IllegalStateException();
    }

    @Accessor("ARMOR_ENTITY_GLINT")
    static RenderType getArmorEntityGlint() {
        throw new IllegalStateException();
    }

    @Accessor("GLINT_DIRECT")
    static RenderType getGlintDirect() {
        throw new IllegalStateException();
    }

    @Accessor("ENTITY_GLINT_DIRECT")
    static RenderType getEntityGlintDirect() {
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
