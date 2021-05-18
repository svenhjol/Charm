package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public interface EntityDropXpCallback {
    Event<EntityDropXpCallback> BEFORE = EventFactory.createArrayBacked(EntityDropXpCallback.class, (listeners) -> (entity) -> {
        for (EntityDropXpCallback listener : listeners) {
            ActionResult result = listener.interact(entity);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(LivingEntity entity);
}
