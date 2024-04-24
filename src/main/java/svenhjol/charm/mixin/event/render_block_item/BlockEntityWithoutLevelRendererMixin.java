package svenhjol.charm.mixin.event.render_block_item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.BlockItemRenderEvent;

import java.util.Optional;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Inject(
        method = "renderByItem",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void hookRender(ItemStack itemStack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j, CallbackInfo ci) {
        var item = itemStack.getItem();

        if (item instanceof BlockItem blockItem) {
            Optional<BlockEntity> blockEntity = BlockItemRenderEvent.INSTANCE.invoke(itemStack, blockItem.getBlock());

            if (blockEntity.isPresent()) {
                blockEntityRenderDispatcher.renderItem(blockEntity.get(), poseStack, bufferSource, i, j);
                ci.cancel();
            }
        }
    }
}
