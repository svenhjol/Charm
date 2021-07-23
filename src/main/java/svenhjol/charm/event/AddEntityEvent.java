package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

public interface AddEntityEvent {
    Event<AddEntityEvent> EVENT = EventFactory.createArrayBacked(AddEntityEvent.class, (listeners) -> (entity) -> {
        for (AddEntityEvent listener : listeners) {
            InteractionResult result = listener.interact(entity);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(Entity entity);
}
