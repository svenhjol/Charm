package svenhjol.charm.feature.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

import java.util.SortedMap;

public final class ClientCallbacks {
    public static SortedMap<RenderType, BufferBuilder> builders;
    public static ItemStack targetStack;
    public static boolean enabled = false;

    public static RenderType getArmorGlintRenderLayer() {
        return ClientHandlers.ARMOR_GLINT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.ARMOR_GLINT);
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ClientHandlers.ARMOR_ENTITY_GLINT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.ARMOR_ENTITY_GLINT);
    }

    public static RenderType getDirectGlintRenderLayer() {
        return ClientHandlers.GLINT_DIRECT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.GLINT_DIRECT);
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return ClientHandlers.ENTITY_GLINT_DIRECT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.ENTITY_GLINT_DIRECT);
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ClientHandlers.ENTITY_GLINT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.ENTITY_GLINT);
    }

    public static RenderType getGlintRenderLayer() {
        return ClientHandlers.GLINT
            .getOrDefault(ColoredGlints.get(targetStack), RenderType.GLINT);
    }
}
