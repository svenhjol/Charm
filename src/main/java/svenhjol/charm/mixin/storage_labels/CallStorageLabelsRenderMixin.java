package svenhjol.charm.mixin.storage_labels;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
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
    private static <T extends BlockEntity> void hookExtraRender(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci, int j) {
        if (blockEntity instanceof ChestBlockEntity) {
            BlockEntityRendererFactory.Context context = StorageLabelsClient.chestBlockEntityContext.get();
            LootableContainerBlockEntityRenderer.render(context.getRenderDispatcher(), blockEntity, tickDelta, matrices, vertexConsumers, j, OverlayTexture.DEFAULT_UV);
        }
    }
}
