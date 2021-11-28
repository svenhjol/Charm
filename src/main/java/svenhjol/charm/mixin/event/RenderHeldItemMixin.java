package svenhjol.charm.mixin.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.RenderHeldItemCallback;

@Mixin(ItemInHandRenderer.class)
public class RenderHeldItemMixin {
    /**
     * Fires the {@link RenderHeldItemCallback} event.
     * This allows custom rendering of the item that appears in the player's hand.
     */
    @Inject(
        method = "renderArmWithItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRenderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack poseStack, MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        InteractionResult result = RenderHeldItemCallback.EVENT.invoker().interact(tickDelta, pitch, hand, swingProgress, item, equipProgress, poseStack, bufferSource, light);
        if (result != InteractionResult.PASS) {
            ci.cancel();
        }
    }
}
