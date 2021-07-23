package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface EntityHurtEvent {
    Event<EntityHurtEvent> EVENT = EventFactory.createArrayBacked(EntityHurtEvent.class, (listeners) -> (entity, source, amount) -> {
        for (EntityHurtEvent listener : listeners) {
            InteractionResult result = listener.interact(entity, source, amount);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(LivingEntity entity, DamageSource source, float amount);
}
