package svenhjol.charm.mixin.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.RenderBlockItemCallback;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class RenderBlockItemMixin {
    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    /**
     * Fires the {@link RenderBlockItemCallback} event, allowing modules to define
     * their own blockItem entity renderers to show in the player's inventory.
     */
    @Inject(
        method = "renderByItem",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void hookRender(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockEntity blockEntity = RenderBlockItemCallback.EVENT.invoker().interact(stack, ((BlockItem) item).getBlock());

            if (blockEntity != null) {
                blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, bufferSource, i, j);
                ci.cancel();
            }
        }
    }
}
