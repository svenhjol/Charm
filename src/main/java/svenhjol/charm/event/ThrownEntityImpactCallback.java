package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;

public interface ThrownEntityImpactCallback {
    Event<ThrownEntityImpactCallback> EVENT = EventFactory.createArrayBacked(ThrownEntityImpactCallback.class, (listeners) -> (entity, hitResult) -> {
        for (ThrownEntityImpactCallback listener : listeners) {
            ActionResult result = listener.interact(entity, hitResult);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(ThrownEntity entity, HitResult hitResult);
}
