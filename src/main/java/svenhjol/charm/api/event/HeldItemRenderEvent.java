package svenhjol.charm.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class HeldItemRenderEvent extends CharmEvent<HeldItemRenderEvent.Handler> {
    public static final HeldItemRenderEvent INSTANCE = new HeldItemRenderEvent();

    private HeldItemRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, MultiBufferSource bufferSource, int light);
    }
}
