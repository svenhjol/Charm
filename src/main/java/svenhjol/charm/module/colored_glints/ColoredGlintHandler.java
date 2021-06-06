package svenhjol.charm.module.colored_glints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.mixin.accessor.BufferBuilderStorageAccessor;
import svenhjol.charm.mixin.accessor.MinecraftClientAccessor;
import svenhjol.charm.mixin.accessor.RenderLayerAccessor;
import svenhjol.charm.mixin.accessor.RenderPhaseAccessor;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import svenhjol.charm.module.colored_glints.ColoredGlints;

import java.util.*;
import java.util.stream.Collectors;

public class ColoredGlintHandler {
    public static final String GLINT_NBT = "charm_glint";

    public static Map<String, ResourceLocation> TEXTURES = new HashMap<>();
    public static Map<String, RenderType> GLINT = new HashMap<>();
    public static Map<String, RenderType> ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderType> DIRECT_GLINT = new HashMap<>();
    public static Map<String, RenderType> DIRECT_ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderType> ARMOR_GLINT = new HashMap<>();
    public static Map<String, RenderType> ARMOR_ENTITY_GLINT = new HashMap<>();

    public static String defaultGlintColor;
    public static ItemStack targetStack;

    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        for (DyeColor dyeColor : DyeColor.values()) {
            String color = dyeColor.getSerializedName();
            TEXTURES.put(color, new ResourceLocation(Charm.MOD_ID, "textures/misc/" + color + "_glint.png"));

            GLINT.put(color, createGlint(color, TEXTURES.get(color)));
            ENTITY_GLINT.put(color, createEntityGlint(color, TEXTURES.get(color)));
            DIRECT_GLINT.put(color, createDirectGlint(color, TEXTURES.get(color)));
            DIRECT_ENTITY_GLINT.put(color, createDirectEntityGlint(color, TEXTURES.get(color)));
            ARMOR_GLINT.put(color, createArmorGlint(color, TEXTURES.get(color)));
            ARMOR_ENTITY_GLINT.put(color, createArmorEntityGlint(color, TEXTURES.get(color)));
        }

        // check that the configured glint color is valid
        List<String> validColors = Arrays.stream(DyeColor.values()).map(DyeColor::getSerializedName).collect(Collectors.toList());
        validColors.add("rainbow");

        defaultGlintColor = (ModuleHandler.enabled(svenhjol.charm.module.colored_glints.ColoredGlints.class) && validColors.contains(svenhjol.charm.module.colored_glints.ColoredGlints.glintColor)) ? ColoredGlints.glintColor : DyeColor.PURPLE.getSerializedName();

        hasInit = true;
    }

    public static String getDefaultGlintColor() {
        return defaultGlintColor;
    }

    public static String getStackColor(ItemStack stack) {
        if (stack != null && stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                if (nbt.contains(GLINT_NBT))
                    return nbt.getString(GLINT_NBT);
            }
        }

        return getDefaultGlintColor();
    }

    public static RenderType getArmorGlintRenderLayer() {
        return ARMOR_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getArmorGlint());
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ARMOR_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getArmorEntityGlint());
    }

    public static RenderType getDirectGlintRenderLayer() {
        return DIRECT_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getDirectGlint());
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return DIRECT_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getDirectEntityGlint());
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getEntityGlint());
    }

    public static RenderType getGlintRenderLayer() {
        return GLINT.getOrDefault(getStackColor(targetStack), RenderLayerAccessor.getGlint());
    }

    private static RenderType createGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getGlintTexturing())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getEntityGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getEntityGlintTexturing())
            .setOutputState(RenderPhaseAccessor.getItemTarget())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getArmorGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getGlintTexturing())
            .setLayeringState(RenderPhaseAccessor.getViewOffsetZLayering())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getArmorEntityGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getEntityGlintTexturing())
            .setLayeringState(RenderPhaseAccessor.getViewOffsetZLayering())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getDirectGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getGlintTexturing())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderPhaseAccessor.getDirectEntityGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderPhaseAccessor.getColorMask())
            .setCullState(RenderPhaseAccessor.getDisableCulling())
            .setDepthTestState(RenderPhaseAccessor.getEqualDepthTest())
            .setTransparencyState(RenderPhaseAccessor.getGlintTransparency())
            .setTexturingState(RenderPhaseAccessor.getEntityGlintTexturing())
            .setOutputState(RenderPhaseAccessor.getItemTarget())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static SortedMap<RenderType, BufferBuilder> getEntityBuilders() {
        RenderBuffers bufferBuilders = ((MinecraftClientAccessor) Minecraft.getInstance()).getBufferBuilders();
        return ((BufferBuilderStorageAccessor)bufferBuilders).getEntityBuilders();
    }
}
