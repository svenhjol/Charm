package svenhjol.charm.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.RenderPhaseAccessor;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ColoredGlintHandler {
    public static final String GLINT_TAG = "charm_glint";
    public static Map<DyeColor, RenderLayer> GLINTS = new HashMap<>();

    public static void init() {
        for (DyeColor dyeColor : DyeColor.values()) {
            GLINTS.put(dyeColor, createGlint(dyeColor, new Identifier(Charm.MOD_ID, "textures/misc/" + dyeColor.getName() + "_glint.png")));
        }
    }

    public static RenderLayer createGlint(DyeColor dyeColor, Identifier texture) {
        return RenderLayer.of("glint_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getGlintTexturing())
            .build(false));
    }

    public static VertexConsumer getCustomGlint(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint, @Nullable ItemStack stack) {
        if (glint) {
            RenderLayer renderGlint = RenderLayer.getGlint();

            if (stack != null && stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    if (tag.contains(GLINT_TAG)) {
                        DyeColor dyeColor = DyeColor.byName(tag.getString(GLINT_TAG), DyeColor.PURPLE);
                        renderGlint = GLINTS.get(dyeColor);
                    }
                }
            }

            return MinecraftClient.isFabulousGraphicsOrBetter() && layer == TexturedRenderLayers.getItemEntityTranslucentCull() ? VertexConsumers.dual(vertexConsumers.getBuffer(RenderLayer.method_30676()), vertexConsumers.getBuffer(layer)) : VertexConsumers.dual(vertexConsumers.getBuffer(solid ? renderGlint : RenderLayer.getEntityGlint()), vertexConsumers.getBuffer(layer));
        } else {
            return vertexConsumers.getBuffer(layer);
        }
    }
}
