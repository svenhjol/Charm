package svenhjol.charm.module.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

@ClientModule(module = ColoredGlints.class)
public class ColoredGlintsClient extends CharmModule {
    public static Map<String, ResourceLocation> TEXTURES = new HashMap<>();
    public static Map<String, RenderType> GLINT = new HashMap<>();
    public static Map<String, RenderType> ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderType> DIRECT_GLINT = new HashMap<>();
    public static Map<String, RenderType> DIRECT_ENTITY_GLINT = new HashMap<>();
    public static Map<String, RenderType> ARMOR_GLINT = new HashMap<>();
    public static Map<String, RenderType> ARMOR_ENTITY_GLINT = new HashMap<>();

    public static boolean hasSetupGlints = false;
    public static ItemStack targetStack;

    @Override
    public void runWhenEnabled() {
        ClientLifecycleEvents.CLIENT_STARTED.register(this::handleClientStarted);
    }

    public static RenderType getArmorGlintRenderLayer() {
        return ARMOR_GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.ARMOR_GLINT);
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ARMOR_ENTITY_GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.ARMOR_ENTITY_GLINT);
    }

    public static RenderType getDirectGlintRenderLayer() {
        return DIRECT_GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.GLINT_DIRECT);
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return DIRECT_ENTITY_GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.ENTITY_GLINT_DIRECT);
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ENTITY_GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.ENTITY_GLINT);
    }

    public static RenderType getGlintRenderLayer() {
        return GLINT.getOrDefault(ColoredGlints.getColoredGlint(targetStack), RenderType.GLINT);
    }

    private void handleClientStarted(Minecraft client) {
        if (hasSetupGlints) return;

        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation texture;
            String color = dyeColor.getSerializedName().toLowerCase(Locale.ROOT);

            if (dyeColor.equals(DyeColor.PURPLE)) {
                texture = new ResourceLocation("textures/misc/enchanted_item_glint.png");
            } else {
                texture = new ResourceLocation(Charm.MOD_ID, "textures/misc/" + color + "_glint.png");
            }
            TEXTURES.put(color, texture);

            GLINT.put(color, createGlint(color, TEXTURES.get(color)));
            ENTITY_GLINT.put(color, createEntityGlint(color, TEXTURES.get(color)));
            DIRECT_GLINT.put(color, createDirectGlint(color, TEXTURES.get(color)));
            DIRECT_ENTITY_GLINT.put(color, createDirectEntityGlint(color, TEXTURES.get(color)));
            ARMOR_GLINT.put(color, createArmorGlint(color, TEXTURES.get(color)));
            ARMOR_ENTITY_GLINT.put(color, createArmorEntityGlint(color, TEXTURES.get(color)));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
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

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static SortedMap<RenderType, BufferBuilder> getEntityBuilders() {
        RenderBuffers renderBuffers = Minecraft.getInstance().renderBuffers();
        return renderBuffers.fixedBuffers;
    }
}
