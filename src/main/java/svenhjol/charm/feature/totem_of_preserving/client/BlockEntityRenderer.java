package svenhjol.charm.feature.totem_of_preserving.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.totem_of_preserving.common.Item;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totem_of_preserving.common.BlockEntity;
import svenhjol.charm.foundation.feature.FeatureResolver;

public class BlockEntityRenderer<T extends BlockEntity> implements net.minecraft.client.renderer.blockentity.BlockEntityRenderer<T>, FeatureResolver<TotemOfPreserving> {
    private final ItemStack stack;

    public BlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.stack = new ItemStack(feature().registers.item.get());
        Item.setGlint(this.stack);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(1f, 1f, 1f);
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        var level = Minecraft.getInstance().level;

        var rotateTicks = entity.getRotateTicks();
        entity.setRotateTicks(rotateTicks += 0.25f);

        if (rotateTicks >= 360f) {
            rotateTicks = 0f;
            entity.setRotateTicks(rotateTicks);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(rotateTicks));
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 0xf000f0, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, level, entity.hashCode());
        poseStack.popPose();
    }

    @Override
    public Class<TotemOfPreserving> typeForFeature() {
        return TotemOfPreserving.class;
    }
}
