package svenhjol.charm.mixin.feature.glint_coloring;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.feature.glint_coloring.client.Handlers;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @ModifyReturnValue(
            method = "armorEntityGlint",
            at = @At("RETURN")
    )
    private static RenderType hookGetArmorEntityGlint(RenderType original) {
        var layer = glintColoring().getArmorEntityGlintRenderLayer();
        return layer != null ? layer : original;
    }

    @ModifyReturnValue(
            method = "entityGlint",
            at = @At("RETURN")
    )
    private static RenderType hookGetEntityGlint(RenderType original) {
        var layer = glintColoring().getEntityGlintRenderLayer();
        return layer != null ? layer : original;
    }

    @ModifyReturnValue(
            method = "entityGlintDirect",
            at = @At("RETURN")
    )
    private static RenderType hookGetEntityGlintDirect(RenderType original) {
        var layer = glintColoring().getDirectEntityGlintRenderLayer();
        return layer != null ? layer : original;
    }

    @ModifyReturnValue(
            method = "glint",
            at = @At("RETURN")
    )
    private static RenderType hookGetGlint(RenderType original) {
        var layer = glintColoring().getGlintRenderLayer();
        return layer != null ? layer : original;
    }

    @Unique
    private static Handlers glintColoring() {
        return Resolve.feature(GlintColoringClient.class).handlers;
    }
}
