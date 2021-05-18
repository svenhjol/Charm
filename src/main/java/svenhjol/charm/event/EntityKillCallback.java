package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityKillCallback {
    Event<EntityKillCallback> EVENT = EventFactory.createArrayBacked(EntityKillCallback.class, (listeners) -> (livingEntity, source) -> {
        for (EntityKillCallback listener : listeners) {
            listener.interact(livingEntity, source);
        }
    });

    void interact(LivingEntity entity, DamageSource source);
}
