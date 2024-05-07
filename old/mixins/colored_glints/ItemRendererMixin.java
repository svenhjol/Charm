package svenhjol.charm.mixin.feature.colored_glints;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.colored_glints.ClientCallbacks;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    /**
     * Fetches the itemstack in the current context (such as player inventory) so that
     * the glint handler can modify its color.
     * Makes no runtime modification to this class.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "HEAD"
        )
    )
    private void hookRenderItem(ItemStack stack, ItemDisplayContext context, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        ClientCallbacks.targetStack = stack;
    }
}
