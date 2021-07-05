package svenhjol.charm.mixin.callback;

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
import svenhjol.charm.event.RenderBlockItemCallback;
import svenhjol.charm.module.colored_glints.ColoredGlintsClient;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class RenderBlockItemCallbackMixin {
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
    private void hookRender(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        ColoredGlintsClient.targetStack = stack; // take reference to item to be rendered

        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockEntity blockEntity = RenderBlockItemCallback.EVENT.invoker().interact(((BlockItem) item).getBlock());

            if (blockEntity != null) {
                this.blockEntityRenderDispatcher.renderItem(blockEntity, matrixStack, vertexConsumerProvider, i, j);
                ci.cancel();
            }
        }
    }
}
