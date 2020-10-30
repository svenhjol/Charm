package svenhjol.charm.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.BlockItemRenderCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    private ItemStack itemStackToRender;

    /**
     * Allows modules to define their own blockItem entity renderers
     * to show in the player's inventory.
     */
    @Inject(
        method = "render",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void hookRender(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        this.itemStackToRender = stack;

        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockEntity blockEntity = BlockItemRenderCallback.EVENT.invoker().interact(((BlockItem) item).getBlock());

            if (blockEntity != null) {
                BlockEntityRenderDispatcher.INSTANCE.renderEntity(blockEntity, matrixStack, vertexConsumerProvider, i, j);
                ci.cancel();
            }
        }
    }

    /**
     * Redirect shield and trident glint consumer calls to Charm's ColoredGlintHandler
     */
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private VertexConsumer hookRenderGetDirectGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return ColoredGlintHandler.getDirectItemGlintConsumer(provider, layer, solid, glint, this.itemStackToRender);
    }
}
