package svenhjol.charm.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface RenderGuiCallback {
    Event<RenderGuiCallback> EVENT = EventFactory.createArrayBacked(RenderGuiCallback.class, (listeners) -> (client, matrices, mouseX, mouseY, delta) -> {
        for (RenderGuiCallback listener : listeners) {
            listener.interact(client, matrices, mouseX, mouseY, delta);
        }
    });

    void interact(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta);
}
