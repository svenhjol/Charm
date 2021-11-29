package svenhjol.charm.module.map_tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;

@ClientModule(module = MapTooltip.class)
public class MapTooltipClient extends CharmModule {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));

    @Override
    public void runWhenEnabled() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(Screen screen, PoseStack poseStack, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && stack.getItem() == Items.FILLED_MAP) {
            renderTooltip(poseStack, stack, lines, x, y);
        }
    }

    private void renderTooltip(PoseStack poseStack, ItemStack stack, List<ClientTooltipComponent> lines, int tx, int ty) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        Integer mapId = MapItem.getMapId(stack);
        if (mapId == null) return;

        MapItemSavedData data = MapItem.getSavedData(mapId, level);
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

        poseStack.pushPose();
        poseStack.translate(x, y, 500.0);
        poseStack.scale(0.5F, 0.5F, 1.0F);
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        final VertexConsumer builder = bufferSource.getBuffer(MAP_BACKGROUND);
        Matrix4f matrix4f = poseStack.last().pose();
        builder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        builder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 1.0);
        mc.gameRenderer.getMapRenderer().render(poseStack, bufferSource, mapId, data, false, light);
        poseStack.popPose();
        poseStack.popPose();
    }
}

