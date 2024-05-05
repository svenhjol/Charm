package svenhjol.charm.feature.totem_of_preserving;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TotemBlockEntityRenderer<T extends TotemBlockEntity> implements BlockEntityRenderer<T> {
    private final ItemStack stack;

    public TotemBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.stack = new ItemStack(TotemOfPreserving.item.get());
        TotemItem.setGlint(this.stack);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1f, 1f, 1f);
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        var level = Minecraft.getInstance().level;

        entity.rotateTicks += 0.25f;
        if (entity.rotateTicks >= 360f) {
            entity.rotateTicks = 0f;
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotateTicks));
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, level, entity.hashCode());
        poseStack.popPose();
    }
}
