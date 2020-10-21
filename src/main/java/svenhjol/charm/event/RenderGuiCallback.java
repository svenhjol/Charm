package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public interface RenderGuiCallback {
    Event<RenderGuiCallback> EVENT = EventFactory.createArrayBacked(RenderGuiCallback.class, (listeners) -> (client, matrices, mouseX, mouseY, delta) -> {
        for (RenderGuiCallback listener : listeners) {
            listener.interact(client, matrices, mouseX, mouseY, delta);
        }
    });

    void interact(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta);
}
