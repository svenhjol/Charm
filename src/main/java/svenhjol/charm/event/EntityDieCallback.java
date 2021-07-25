package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface EntityDieCallback {
    Event<EntityDieCallback> EVENT = EventFactory.createArrayBacked(EntityDieCallback.class, (listeners) -> (livingEntity, source) -> {
        for (EntityDieCallback listener : listeners) {
            listener.interact(livingEntity, source);
        }
    });

    void interact(LivingEntity entity, DamageSource source);
}
