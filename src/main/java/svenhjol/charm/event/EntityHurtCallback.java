package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityHurtCallback {
    Event<EntityHurtCallback> EVENT = EventFactory.createArrayBacked(EntityHurtCallback.class, (listeners) -> (entity, source, amount) -> {
        for (EntityHurtCallback listener : listeners) {
            ActionResult result = listener.interact(entity, source, amount);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(LivingEntity entity, DamageSource source, float amount);
}
