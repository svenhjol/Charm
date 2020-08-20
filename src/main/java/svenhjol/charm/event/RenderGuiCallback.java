package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface RenderGuiCallback {
    Event<RenderGuiCallback> EVENT = EventFactory.createArrayBacked(RenderGuiCallback.class, (listeners) -> (client, matrices, mouseX, mouseY, delta) -> {
        for (RenderGuiCallback listener : listeners) {
            ActionResult result = listener.interact(client, matrices, mouseX, mouseY, delta);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta);
}
