package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface EntityJumpCallback {
    Event<EntityJumpCallback> EVENT = EventFactory.createArrayBacked(EntityJumpCallback.class, (listeners) -> entity -> {
        for (EntityJumpCallback listener : listeners) {
            listener.interact(entity);
        }
    });

    void interact(LivingEntity entity);
}
