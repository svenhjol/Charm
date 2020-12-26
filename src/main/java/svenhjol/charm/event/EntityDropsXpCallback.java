package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityDropsXpCallback {
    Event<EntityDropsXpCallback> BEFORE = EventFactory.createArrayBacked(EntityDropsXpCallback.class, (listeners) -> (entity) -> {
        for (EntityDropsXpCallback listener : listeners) {
            ActionResult result = listener.interact(entity);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(LivingEntity entity);
}
