package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface EntityKillEvent {
    Event<EntityKillEvent> EVENT = EventFactory.createArrayBacked(EntityKillEvent.class, (listeners) -> (livingEntity, source) -> {
        for (EntityKillEvent listener : listeners) {
            listener.interact(livingEntity, source);
        }
    });

    void interact(LivingEntity entity, DamageSource source);
}
