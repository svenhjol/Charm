package svenhjol.charm.mixin.storage_labels;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;
import svenhjol.charm.module.storage_labels.LootableContainerBlockEntityRenderer;

@Mixin(BlockEntityRenderDispatcher.class)
public class CallStorageLabelsRenderMixin {

    /**
     * After rendering vanilla block entities, call the storage labels render
     * if the block entity is of type chest.
     *
     * We can't hook directly into the ChestBlockEntityRenderer because of an
     * intermittent method desc match failure.
     */
    @Inject(
        method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static <T extends BlockEntity> void hookExtraRender(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci, int j) {
        if (blockEntity instanceof ChestBlockEntity) {
            BlockEntityRendererProvider.Context context = StorageLabelsClient.chestBlockEntityContext.get();
            LootableContainerBlockEntityRenderer.render(context.getBlockEntityRenderDispatcher(), blockEntity, tickDelta, matrices, vertexConsumers, j, OverlayTexture.NO_OVERLAY);
        }
    }
}
