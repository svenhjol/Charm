package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface EntityJumpEvent {
    Event<EntityJumpEvent> EVENT = EventFactory.createArrayBacked(EntityJumpEvent.class, (listeners) -> entity -> {
        for (EntityJumpEvent listener : listeners) {
            listener.interact(entity);
        }
    });

    void interact(LivingEntity entity);
}
