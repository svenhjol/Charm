package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

public interface EntityDropXpCallback {
    Event<EntityDropXpCallback> BEFORE = EventFactory.createArrayBacked(EntityDropXpCallback.class, (listeners) -> (entity) -> {
        for (EntityDropXpCallback listener : listeners) {
            InteractionResult result = listener.interact(entity);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(LivingEntity entity);
}
