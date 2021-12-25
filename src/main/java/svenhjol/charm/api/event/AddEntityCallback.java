package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

public interface AddEntityCallback {
    Event<AddEntityCallback> EVENT = EventFactory.createArrayBacked(AddEntityCallback.class, (listeners) -> entity -> {
        for (AddEntityCallback listener : listeners) {
            InteractionResult result = listener.interact(entity);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(Entity entity);
}
