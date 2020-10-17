package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityDeathCallback {
    Event<EntityDeathCallback> EVENT = EventFactory.createArrayBacked(EntityDeathCallback.class, (listeners) -> (livingEntity, source) -> {
        for (EntityDeathCallback listener : listeners) {
            listener.interact(livingEntity, source);
        }
    });

    void interact(LivingEntity entity, DamageSource source);
}
