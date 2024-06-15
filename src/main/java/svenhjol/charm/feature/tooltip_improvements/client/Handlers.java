package svenhjol.charm.feature.tooltip_improvements.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.feature.tooltip_improvements.TooltipImprovements;
import svenhjol.charm.charmony.feature.FeatureHolder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class Handlers extends FeatureHolder<TooltipImprovements> {
    public static final RenderType MAP_BACKGROUND
        = RenderType.text(new ResourceLocation("textures/map/map_background.png"));

    public Handlers(TooltipImprovements feature) {
        super(feature);
    }

    /**
     * Show a rendered map when hovering over a filled map item.
     */
    public void hoverOverMap(GuiGraphics guiGraphics, List<ClientTooltipComponent> components, int tx, int ty, @Nullable ItemStack stack) {
        if (stack != null && stack.getItem() == Items.FILLED_MAP) {
            var client = Minecraft.getInstance();
            var level = client.level;
            if (level == null) return;

            var mapId = MapItem.getMapId(stack);
            if (mapId == null) return;

            var data = MapItem.getSavedData(mapId, level);
            if (data == null) return;

            ty -= 16;

            var x = tx;
            var y = ty - 72;
            var w = 64;
            var light = 240;
            var right = x + w;

            if (right > client.getWindow().getGuiScaledWidth()) {
                x = client.getWindow().getGuiScaledWidth() - w;
            }

            if (y < 0) {
                y = ty + components.size() * 10 + 8;
            }

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 500.0);
            guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);

            var bufferSource = client.renderBuffers().bufferSource();
            var vertexConsumer = bufferSource.getBuffer(MAP_BACKGROUND);
            var matrix4f = guiGraphics.pose().last().pose();

            vertexConsumer.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
            vertexConsumer.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
            vertexConsumer.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
            vertexConsumer.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0, 0.0, 1.0);

            client.gameRenderer.getMapRenderer().render(guiGraphics.pose(), bufferSource, mapId, data, false, light);

            guiGraphics.pose().popPose();
            guiGraphics.pose().popPose();
        }
    }

    /**
     * When hovering over a shulker box, remove all lines that show the contents.
     */
    public void removeLinesFromShulkerBox(ItemStack stack, List<Component> components, TooltipFlag tooltipFlag) {
        var shulkerBoxBlock = tryGetShulkerBoxBlock(stack);
        if (shulkerBoxBlock != null && components.size() > 1) {
            var title = components.get(0);
            components.clear();
            components.add(0, title);
        }
    }

    /**
     * When hovering over a shulker box, display a custom grid.
     */
    public Optional<TooltipComponent> addGridToShulkerBox(ItemStack stack) {
        var shulkerBoxBlock = tryGetShulkerBoxBlock(stack);
        var level = Minecraft.getInstance().level;
        if (level != null && shulkerBoxBlock != null) {
            var tag = BlockItem.getBlockEntityData(stack);
            if (tag != null) {
                var blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), tag);

                if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                    NonNullList<ItemStack> items = shulkerBox.itemStacks;
                    return Optional.of(new ShulkerBoxItemTooltip(items));
                }
            }
        }

        return Optional.empty();
    }

    @Nullable
    public ShulkerBoxBlock tryGetShulkerBoxBlock(ItemStack stack) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
            return shulkerBoxBlock;
        }
        return null;
    }
}
