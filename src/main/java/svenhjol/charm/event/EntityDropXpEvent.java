package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

public interface EntityDropXpEvent {
    Event<EntityDropXpEvent> BEFORE = EventFactory.createArrayBacked(EntityDropXpEvent.class, (listeners) -> (entity) -> {
        for (EntityDropXpEvent listener : listeners) {
            InteractionResult result = listener.interact(entity);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(LivingEntity entity);
}
