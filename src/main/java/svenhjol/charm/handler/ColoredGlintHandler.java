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

import java.util.*;
import java.util.stream.Collectors;

public class ColoredGlintHandler {
    public static final String GLINT_TAG = "charm_glint";

    public static Map<String, Identifier> TEXTURES = new HashMap<>();
    public static Map<String, RenderLayer> GLINT = new HashMap<>();
    public static Map<String, RenderLayer> ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderLayer> DIRECT_GLINT = new HashMap<>();
    public static Map<String, RenderLayer> DIRECT_ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderLayer> ARMOR_GLINT = new HashMap<>();
    public static Map<String, RenderLayer> ARMOR_ENTITY_GLINT = new HashMap<>();

    public static String defaultGlintColor;
    public static ItemStack targetStack;

    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        for (DyeColor dyeColor : DyeColor.values()) {
            String color = dyeColor.asString();
            TEXTURES.put(color, new Identifier(Charm.MOD_ID, "textures/misc/" + color + "_glint.png"));

            GLINT.put(color, createGlint(color, TEXTURES.get(color)));
            ENTITY_GLINT.put(color, createEntityGlint(color, TEXTURES.get(color)));
            DIRECT_GLINT.put(color, createDirectGlint(color, TEXTURES.get(color)));
            DIRECT_ENTITY_GLINT.put(color, createDirectEntityGlint(color, TEXTURES.get(color)));
            ARMOR_GLINT.put(color, createArmorGlint(color, TEXTURES.get(color)));
            ARMOR_ENTITY_GLINT.put(color, createArmorEntityGlint(color, TEXTURES.get(color)));
        }

        // check that the configured glint color is valid
        List<String> validColors = Arrays.stream(DyeColor.values()).map(DyeColor::asString).collect(Collectors.toList());
        validColors.add("rainbow");

        defaultGlintColor = validColors.contains(Core.glintColor) ? Core.glintColor : DyeColor.PURPLE.asString();

        hasInit = true;
    }

    public static String getDefaultGlintColor() {
        return defaultGlintColor;
    }

    public static String getStackColor(ItemStack stack) {
        if (stack != null && stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                if (tag.contains(GLINT_TAG))
                    return tag.getString(GLINT_TAG);
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

    private static RenderLayer createGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("glint_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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

    private static RenderLayer createEntityGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("entity_glint_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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

    private static RenderLayer createArmorGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("armor_glint_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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

    private static RenderLayer createArmorEntityGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("armor_entity_glint_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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

    private static RenderLayer createDirectGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("glint_direct_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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

    private static RenderLayer createDirectEntityGlint(String color, Identifier texture) {
        RenderLayer renderLayer = RenderLayer.of("entity_glint_direct_" + color, VertexFormats.POSITION_TEXTURE, 7, 256, RenderLayer.MultiPhaseParameters.builder()
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
