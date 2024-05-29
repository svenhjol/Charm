package svenhjol.charm.charmony.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

/**
 * A custom Fabric event that is fired every (living) entity tick.
 */
public interface EntityTickCallback {
    Event<EntityTickCallback> EVENT = EventFactory.createArrayBacked(EntityTickCallback.class, (listeners) -> (entity) -> {
        for (EntityTickCallback listener : listeners) {
            listener.interact(entity);
        }
    });

    void interact(LivingEntity entity);
}
