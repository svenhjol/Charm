package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("unused")
public interface ClientStateUpdateCallback {
    Event<ClientStateUpdateCallback> EVENT = EventFactory.createArrayBacked(ClientStateUpdateCallback.class, listeners -> tag -> {
        for (ClientStateUpdateCallback listener : listeners) {
            listener.interact(tag);
        }
    });

    void interact(CompoundTag tag);
}
