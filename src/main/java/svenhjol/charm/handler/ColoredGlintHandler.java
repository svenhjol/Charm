package svenhjol.charm.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.BufferBuilderStorageAccessor;
import svenhjol.charm.mixin.accessor.MinecraftClientAccessor;
import svenhjol.charm.mixin.accessor.RenderLayerAccessor;
import svenhjol.charm.mixin.accessor.RenderPhaseAccessor;
import svenhjol.charm.module.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class ColoredGlintHandler {
    public static final String GLINT_TAG = "charm_glint";

    public static Map<DyeColor, Identifier> TEXTURES = new HashMap<>();
    public static Map<DyeColor, RenderLayer> GLINT = new HashMap<>();
    public static Map<DyeColor, RenderLayer> ENTITY_GLINT = new HashMap<>();
    public static Map<DyeColor, RenderLayer> DIRECT_GLINT = new HashMap<>();
    public static Map<DyeColor, RenderLayer> DIRECT_ENTITY_GLINT = new HashMap<>();
    public static Map<DyeColor, RenderLayer> ARMOR_GLINT = new HashMap<>();
    public static Map<DyeColor, RenderLayer> ARMOR_ENTITY_GLINT = new HashMap<>();

    public static boolean isEnabled;
    public static DyeColor defaultGlintColor;
    public static ItemStack targetStack;

    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        for (DyeColor dyeColor : DyeColor.values()) {
            TEXTURES.put(dyeColor, new Identifier(Charm.MOD_ID, "textures/misc/" + dyeColor.getName() + "_glint.png"));

            GLINT.put(dyeColor, createGlint(dyeColor, TEXTURES.get(dyeColor)));
            ENTITY_GLINT.put(dyeColor, createEntityGlint(dyeColor, TEXTURES.get(dyeColor)));
            DIRECT_GLINT.put(dyeColor, createDirectGlint(dyeColor, TEXTURES.get(dyeColor)));
            DIRECT_ENTITY_GLINT.put(dyeColor, createDirectEntityGlint(dyeColor, TEXTURES.get(dyeColor)));
            ARMOR_GLINT.put(dyeColor, createArmorGlint(dyeColor, TEXTURES.get(dyeColor)));
            ARMOR_ENTITY_GLINT.put(dyeColor, createArmorEntityGlint(dyeColor, TEXTURES.get(dyeColor)));
        }

        defaultGlintColor = DyeColor.byName(Core.glintColor, DyeColor.PURPLE);

        hasInit = true;
    }

    public static DyeColor getDefaultGlintColor() {
        return defaultGlintColor;
    }

    public static DyeColor getStackColor(ItemStack stack) {
        if (stack != null && stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                if (tag.contains(GLINT_TAG))
                    return DyeColor.byName(tag.getString(GLINT_TAG), DyeColor.PURPLE);
            }
        }

        return getDefaultGlintColor();
    }

    public static RenderLayer getArmorGlintRenderLayer() {
        return ARMOR_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getArmorGlint());
    }

    public static RenderLayer getArmorEntityGlintRenderLayer() {
        return ARMOR_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getArmorEntityGlint());
    }

    public static RenderLayer getDirectGlintRenderLayer() {
        return DIRECT_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getDirectGlint());
    }

    public static RenderLayer getDirectEntityGlintRenderLayer() {
        return DIRECT_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getDirectEntityGlint());
    }

    public static RenderLayer getEntityGlintRenderLayer() {
        return ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getEntityGlint());
    }

    public static RenderLayer getGlintRenderLayer() {
        return GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getGlint());
    }

    private static RenderLayer createGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("glint_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getGlintTexturing())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static RenderLayer createEntityGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("entity_glint_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getEntityGlintTexturing())
            .target(RenderPhaseAccessor.getItemTarget())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static RenderLayer createArmorGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("armor_glint_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getGlintTexturing())
            .layering(RenderPhaseAccessor.getViewOffsetZLayering())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static RenderLayer createArmorEntityGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("armor_entity_glint_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getEntityGlintTexturing())
            .layering(RenderPhaseAccessor.getViewOffsetZLayering())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static RenderLayer createDirectGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("glint_direct_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getGlintTexturing())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static RenderLayer createDirectEntityGlint(DyeColor dyeColor, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("entity_glint_direct_" + dyeColor.getName(), VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, true, false))
            .writeMaskState(RenderPhaseAccessor.getColorMask())
            .cull(RenderPhaseAccessor.getDisableCulling())
            .depthTest(RenderPhaseAccessor.getEqualDepthTest())
            .transparency(RenderPhaseAccessor.getGlintTransparency())
            .texturing(RenderPhaseAccessor.getEntityGlintTexturing())
            .target(RenderPhaseAccessor.getItemTarget())
            .build(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
        return renderLayer;
    }

    private static SortedMap<RenderLayer, BufferBuilder> getEntityBuilders() {
        BufferBuilderStorage bufferBuilders = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getBufferBuilders();
        return ((BufferBuilderStorageAccessor)bufferBuilders).getEntityBuilders();
    }
}
