package svenhjol.charm.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public interface RenderGuiEvent {
    Event<RenderGuiEvent> EVENT = EventFactory.createArrayBacked(RenderGuiEvent.class, (listeners) -> (client, matrices, mouseX, mouseY, delta) -> {
        for (RenderGuiEvent listener : listeners) {
            listener.interact(client, matrices, mouseX, mouseY, delta);
        }
    });

    void interact(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta);
}
