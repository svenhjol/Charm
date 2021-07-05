package svenhjol.charm.mixin.colored_glints;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.colored_glints.ColoredGlintsClient;

@Mixin(ItemRenderer.class)
public class GetItemMixin {
    /**
     * Fetches the itemstack in the current context (such as player inventory) so that
     * the glint handler can modify its color.
     *
     * Makes no runtime modification to this class.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "HEAD"
        )
    )
    private void hookRenderItem(ItemStack stack, ItemTransforms.TransformType renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        ColoredGlintsClient.targetStack = stack;
    }
}
