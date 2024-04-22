package svenhjol.charm.feature.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.colored_glints.register.ClientRegister;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BooleanSupplier;

public class ColoredGlintsClient extends ClientFeature {
    public static final Map<DyeColor, ResourceLocation> ITEM_TEXTURES = new HashMap<>();
    public static final Map<DyeColor, ResourceLocation> ENTITY_TEXTURES = new HashMap<>();
    public static final Map<DyeColor, RenderType> GLINT = new HashMap<>();
    public static final Map<DyeColor, RenderType> ENTITY_GLINT = new HashMap<>();
    public static final Map<DyeColor, RenderType> GLINT_DIRECT = new HashMap<>();
    public static final Map<DyeColor, RenderType> ENTITY_GLINT_DIRECT = new HashMap<>();
    public static final Map<DyeColor, RenderType> ARMOR_GLINT = new HashMap<>();
    public static final Map<DyeColor, RenderType> ARMOR_ENTITY_GLINT = new HashMap<>();

    public static boolean hasSetupGlints = false;
    public static SortedMap<RenderType, BufferBuilder> renderBufferBuilders;
    public static ItemStack targetStack;
    public static boolean enabled = false;

    @Configurable(
        name = "Default glint color",
        description = """
            Overrides the default enchantment glint color.
            Must be a valid dye color name.""",
        requireRestart = false
    )
    public static String defaultGlintColor = DyeColor.PURPLE.getSerializedName();

    @Override
    public String description() {
        return """
            Allows the default enchantment glint color to be customized.
            An item with its own custom enchantment glint color will not be overridden by this feature.""";
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> {
            // Ensure uppercase dye name when retrieving value from config.
            defaultGlintColor = defaultGlintColor.toUpperCase(Locale.ROOT);
            return true;
        });
    }

    @Override
    public void onEnabled() {
        enabled = true;
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public static DyeColor getColoredGlint(@Nullable ItemStack stack) {
        if (!hasColoredGlint(stack)) {
            return getDefaultGlintColor();
        }

        return stack.get(DataComponents.BASE_COLOR);
    }

    /**
     * Check if stack has a colored glint.
     */
    @SuppressWarnings("unused")
    public static boolean hasColoredGlint(@Nullable ItemStack stack) {
        return stack != null && stack.has(DataComponents.BASE_COLOR);
    }

    /**
     * Get the configured default dye color.
     */
    @SuppressWarnings("unused")
    public static DyeColor getDefaultGlintColor() {
        // TODO: cache me
        return EnumHelper.getValueOrDefault(
            () -> DyeColor.valueOf(defaultGlintColor), DyeColor.PURPLE);
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new ClientRegister(this));
    }

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ColoredGlints.class;
    }

    public static RenderType getArmorGlintRenderLayer() {
        return ARMOR_GLINT.getOrDefault(getColoredGlint(targetStack), RenderType.ARMOR_GLINT);
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ARMOR_ENTITY_GLINT.getOrDefault(getColoredGlint(targetStack), RenderType.ARMOR_ENTITY_GLINT);
    }

    public static RenderType getDirectGlintRenderLayer() {
        return GLINT_DIRECT.getOrDefault(getColoredGlint(targetStack), RenderType.GLINT_DIRECT);
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return ENTITY_GLINT_DIRECT.getOrDefault(getColoredGlint(targetStack), RenderType.ENTITY_GLINT_DIRECT);
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ENTITY_GLINT.getOrDefault(getColoredGlint(targetStack), RenderType.ENTITY_GLINT);
    }

    public static RenderType getGlintRenderLayer() {
        return GLINT.getOrDefault(getColoredGlint(targetStack), RenderType.GLINT);
    }

    @SuppressWarnings("unused")
    public void handleClientStarted(Minecraft client) {
        // renderBufferBuilders will be null if the mixins have been blacklisted.
        if (hasSetupGlints || renderBufferBuilders == null) return;

        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation itemTexture;
            ResourceLocation entityTexture;
            var colorName = dyeColor.getSerializedName().toLowerCase(Locale.ROOT);

            if (dyeColor.equals(DyeColor.PURPLE)) {
                itemTexture = ItemRenderer.ENCHANTED_GLINT_ITEM;
                entityTexture = ItemRenderer.ENCHANTED_GLINT_ENTITY;
            } else {
                itemTexture = Charm.id("textures/misc/glints/" + colorName + "_glint.png");
                entityTexture = Charm.id("textures/misc/glints/" + colorName + "_glint.png");
            }

            ITEM_TEXTURES.put(dyeColor, itemTexture);
            ENTITY_TEXTURES.put(dyeColor, entityTexture);

            GLINT.put(dyeColor, createGlint(colorName, ITEM_TEXTURES.get(dyeColor)));
            GLINT_DIRECT.put(dyeColor, createDirectGlint(colorName, ITEM_TEXTURES.get(dyeColor)));
            ENTITY_GLINT.put(dyeColor, createEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
            ENTITY_GLINT_DIRECT.put(dyeColor, createDirectEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
            ARMOR_GLINT.put(dyeColor, createArmorGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
            ARMOR_ENTITY_GLINT.put(dyeColor, createArmorEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
        }

        hasSetupGlints = true;
    }

    private static RenderType createGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.GLINT_TEXTURING)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
            .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ARMOR_GLINT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.GLINT_TEXTURING)
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_GLINT_DIRECT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.GLINT_TEXTURING)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .setCullState(RenderStateShard.NO_CULL)
            .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
            .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
            .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
            .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
            .createCompositeState(false));

        getBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static SortedMap<RenderType, BufferBuilder> getBuilders() {
        return renderBufferBuilders;
    }
}
