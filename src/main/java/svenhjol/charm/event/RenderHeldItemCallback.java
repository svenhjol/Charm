package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public interface RenderHeldItemCallback {
    Event<RenderHeldItemCallback> EVENT = EventFactory.createArrayBacked(RenderHeldItemCallback.class, (listeners) -> (tickDelta, pitch, hand, swingProgress, stack, equipProgress, matrices, vertexConsumers, light) -> {
        for (RenderHeldItemCallback listener : listeners) {
            ActionResult result = listener.interact(tickDelta, pitch, hand, swingProgress, stack, equipProgress, matrices, vertexConsumers, light);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}
