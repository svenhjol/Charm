package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityDropsCallback {
    Event<EntityDropsCallback> EVENT = EventFactory.createArrayBacked(EntityDropsCallback.class, (listeners) -> (entity, source, lootingLevel) -> {
        for (EntityDropsCallback listener : listeners) {
            ActionResult result = listener.interact(entity, source, lootingLevel);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(LivingEntity entity, DamageSource source, int lootingLevel);
}
