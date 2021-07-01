package svenhjol.charm.module.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.MinecraftAccessor;
import svenhjol.charm.mixin.accessor.RenderBuffersAccessor;
import svenhjol.charm.mixin.accessor.RenderStateShardAccessor;
import svenhjol.charm.mixin.accessor.RenderTypeAccessor;
import svenhjol.charm.loader.ClientModule;

import java.util.*;
import java.util.stream.Collectors;

@svenhjol.charm.annotation.ClientModule(module = ColoredGlints.class)
public class ColoredGlintsClient extends ClientModule {
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

    private static boolean hasSetupGlints = false;

    @Override
    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> setupGlints());
    }

    private static void setupGlints() {
        if (hasSetupGlints) return;

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

        defaultGlintColor = (Charm.LOADER.isEnabled(ColoredGlints.class) && validColors.contains(ColoredGlints.glintColor)) ? ColoredGlints.glintColor : DyeColor.PURPLE.getSerializedName();

        hasSetupGlints = true;
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
        return ARMOR_GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getArmorGlint());
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ARMOR_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getArmorEntityGlint());
    }

    public static RenderType getDirectGlintRenderLayer() {
        return DIRECT_GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getGlintDirect());
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return DIRECT_ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getEntityGlintDirect());
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ENTITY_GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getEntityGlint());
    }

    public static RenderType getGlintRenderLayer() {
        return GLINT.getOrDefault(getStackColor(targetStack), RenderTypeAccessor.getGlint());
    }

    private static RenderType createGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getGlintTexturing())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getEntityGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getEntityGlintTexturing())
            .setOutputState(RenderStateShardAccessor.getItemEntityTarget())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getArmorGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getGlintTexturing())
            .setLayeringState(RenderStateShardAccessor.getViewOffsetZLayering())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createArmorEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("armor_entity_glint_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getArmorEntityGlintShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getEntityGlintTexturing())
            .setLayeringState(RenderStateShardAccessor.getViewOffsetZLayering())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getGlintDirectShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getGlintTexturing())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static RenderType createDirectEntityGlint(String color, ResourceLocation texture) {
        RenderType renderLayer = RenderType.create("entity_glint_direct_" + color, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(RenderStateShardAccessor.getEntityGlintDirectShader())
            .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
            .setWriteMaskState(RenderStateShardAccessor.getColorWrite())
            .setCullState(RenderStateShardAccessor.getNoCull())
            .setDepthTestState(RenderStateShardAccessor.getEqualDepthTest())
            .setTransparencyState(RenderStateShardAccessor.getGlintTransparency())
            .setTexturingState(RenderStateShardAccessor.getEntityGlintTexturing())
            .setOutputState(RenderStateShardAccessor.getItemEntityTarget())
            .createCompositeState(false));

        getEntityBuilders().put(renderLayer, new BufferBuilder(renderLayer.bufferSize()));
        return renderLayer;
    }

    private static SortedMap<RenderType, BufferBuilder> getEntityBuilders() {
        RenderBuffers bufferBuilders = ((MinecraftAccessor) Minecraft.getInstance()).getRenderBuffers();
        return ((RenderBuffersAccessor)bufferBuilders).getFixedBuffers();
    }
}
