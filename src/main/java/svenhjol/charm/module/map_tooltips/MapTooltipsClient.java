package svenhjol.charm.module.map_tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipEvent;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;

@ClientModule(module = MapTooltips.class)
public class MapTooltipsClient extends CharmModule {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));

    @Override
    public void runWhenEnabled() {
        RenderTooltipEvent.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && stack.getItem() == Items.FILLED_MAP) {
            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int tx, int ty) {
        if (stack == null || stack.getItem() != Items.FILLED_MAP) return;

        final Minecraft mc = Minecraft.getInstance();
        final Level world = mc.level;
        if (world == null) return;

        Integer mapId = MapItem.getMapId(stack);
        if (mapId == null) return;

        MapItemSavedData data = MapItem.getSavedData(mapId, world);
        if (data == null) return;

        ty -= 16;

        int x = tx;
        int y = ty - 72;
        int w = 64;
        int right = x + w;

        if (right > mc.getWindow().getGuiScaledWidth())
            x = mc.getWindow().getGuiScaledWidth() - w;

        if (y < 0)
            y = ty + lines.size() * 10 + 8;

        int light = 240;

        matrices.pushPose();
        matrices.translate(x, y, 500.0);
        matrices.scale(0.5F, 0.5F, 1.0F);
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        final VertexConsumer builder = bufferSource.getBuffer(MAP_BACKGROUND);
        Matrix4f matrix4f = matrices.last().pose();
        builder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        matrices.pushPose();
        matrices.translate(0.0, 0.0, 1.0);
        mc.gameRenderer.getMapRenderer().render(matrices, bufferSource, mapId, data, false, light);
        matrices.popPose();
        matrices.popPose();
    }
}

