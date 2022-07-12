package svenhjol.charm.module.totem_of_preserving;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;

public class TotemBlockEntityRenderer<T extends TotemBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;
    private final ItemStack stack;

    public TotemBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.stack = new ItemStack(TotemOfPreserving.ITEM);
        TotemOfPreservingItem.setGlint(this.stack, true);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.5F, 0.5F, 0.5F);

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (itemRenderer == null) return;

        entity.rotateTicks += 0.25F;
        if (entity.rotateTicks >= 360F) {
            entity.rotateTicks = 0F;
        }

        poseStack.mulPose(Vector3f.YP.rotationDegrees(entity.rotateTicks));
        itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, 255, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.hashCode());
        poseStack.popPose();
    }
}
