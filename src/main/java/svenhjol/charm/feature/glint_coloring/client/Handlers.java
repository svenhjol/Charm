package svenhjol.charm.feature.glint_coloring.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.foundation.feature.FeatureHolder;

import javax.annotation.Nullable;
import java.util.*;

public final class Handlers extends FeatureHolder<GlintColoringClient> {
    public final Map<DyeColor, ResourceLocation> ITEM_TEXTURES = new HashMap<>();
    public final Map<DyeColor, ResourceLocation> ENTITY_TEXTURES = new HashMap<>();
    public final Map<DyeColor, RenderType> GLINT = new HashMap<>();
    public final Map<DyeColor, RenderType> ENTITY_GLINT = new HashMap<>();
    public final Map<DyeColor, RenderType> ENTITY_GLINT_DIRECT = new HashMap<>();
    public final Map<DyeColor, RenderType> ARMOR_ENTITY_GLINT = new HashMap<>();

    private SortedMap<RenderType, ByteBufferBuilder> builders;
    private ItemStack targetStack;
    private boolean enabled = false;

    public static boolean initialized = false;

    public Handlers(GlintColoringClient feature) {
        super(feature);
    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setTargetStack(@Nullable ItemStack targetStack) {
        this.targetStack = targetStack;
    }

    public void setBuilders(@Nullable SortedMap<RenderType, ByteBufferBuilder> builders) {
        this.builders = builders;
        if (initialized || builders == null) return;

        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation itemTexture;
            ResourceLocation entityTexture;
            var colorName = dyeColor.getSerializedName().toLowerCase(Locale.ROOT);

            if (dyeColor.equals(DyeColor.PURPLE)) {
                itemTexture = ItemRenderer.ENCHANTED_GLINT_ITEM;
                entityTexture = ItemRenderer.ENCHANTED_GLINT_ENTITY;
            } else {
                itemTexture = Charm.id("textures/misc/enchanted_glints/" + colorName + "_glint.png");
                entityTexture = Charm.id("textures/misc/enchanted_glints/" + colorName + "_glint.png");
            }

            ITEM_TEXTURES.put(dyeColor, itemTexture);
            ENTITY_TEXTURES.put(dyeColor, entityTexture);

            GLINT.put(dyeColor, createGlint(colorName, ITEM_TEXTURES.get(dyeColor)));
            ENTITY_GLINT.put(dyeColor, createEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
            ENTITY_GLINT_DIRECT.put(dyeColor, createDirectEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
            ARMOR_ENTITY_GLINT.put(dyeColor, createArmorEntityGlint(colorName, ENTITY_TEXTURES.get(dyeColor)));
        }

        initialized = true;
    }

    public RenderType createGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_" + color,
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(RenderStateShard.GLINT_TEXTURING)
                .createCompositeState(false));

        getBuilders().put(renderLayer, new ByteBufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    public RenderType createEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_" + color,
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false));

        getBuilders().put(renderLayer, new ByteBufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    public RenderType createArmorEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_entity_glint_" + color,
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(false));

        getBuilders().put(renderLayer, new ByteBufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    public RenderType createDirectEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_direct_" + color,
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(RenderStateShard.ENTITY_GLINT_TEXTURING)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .createCompositeState(false));

        getBuilders().put(renderLayer, new ByteBufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    public SortedMap<RenderType, ByteBufferBuilder> getBuilders() {
        return builders;
    }

    @Nullable
    public RenderType getArmorEntityGlintRenderLayer() {
        var color = GlintColoring.get(targetStack);
        return ARMOR_ENTITY_GLINT.get(color);
    }

    @Nullable
    public RenderType getDirectEntityGlintRenderLayer() {
        var color = GlintColoring.get(targetStack);
        return ENTITY_GLINT_DIRECT.get(color);
    }

    @Nullable
    public RenderType getEntityGlintRenderLayer() {
        var color = GlintColoring.get(targetStack);
        return ENTITY_GLINT.get(color);
    }

    @Nullable
    public RenderType getGlintRenderLayer() {
        var color = GlintColoring.get(targetStack);
        return GLINT.get(color);
    }
}
