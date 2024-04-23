package svenhjol.charm.feature.colored_glints;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

import java.util.SortedMap;

public class ClientCallbacks {
    public static SortedMap<RenderType, BufferBuilder> builders;
    public static ItemStack targetStack;

    public static RenderType getArmorGlintRenderLayer() {
        return ClientHandler.ARMOR_GLINT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.ARMOR_GLINT);
    }

    public static RenderType getArmorEntityGlintRenderLayer() {
        return ClientHandler.ARMOR_ENTITY_GLINT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.ARMOR_ENTITY_GLINT);
    }

    public static RenderType getDirectGlintRenderLayer() {
        return ClientHandler.GLINT_DIRECT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.GLINT_DIRECT);
    }

    public static RenderType getDirectEntityGlintRenderLayer() {
        return ClientHandler.ENTITY_GLINT_DIRECT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.ENTITY_GLINT_DIRECT);
    }

    public static RenderType getEntityGlintRenderLayer() {
        return ClientHandler.ENTITY_GLINT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.ENTITY_GLINT);
    }

    public static RenderType getGlintRenderLayer() {
        return ClientHandler.GLINT
            .getOrDefault(ColoredGlintsClient.get(targetStack), RenderType.GLINT);
    }
}
