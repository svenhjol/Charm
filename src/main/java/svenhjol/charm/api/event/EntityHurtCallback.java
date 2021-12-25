package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface EntityHurtCallback {
    Event<EntityHurtCallback> EVENT = EventFactory.createArrayBacked(EntityHurtCallback.class, (listeners) -> (entity, source, amount) -> {
        for (EntityHurtCallback listener : listeners) {
            InteractionResult result = listener.interact(entity, source, amount);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(LivingEntity entity, DamageSource source, float amount);
}
