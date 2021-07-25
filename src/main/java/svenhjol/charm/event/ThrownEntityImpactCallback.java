package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;

public interface ThrownEntityImpactCallback {
    Event<ThrownEntityImpactCallback> EVENT = EventFactory.createArrayBacked(ThrownEntityImpactCallback.class, (listeners) -> (entity, hitResult) -> {
        for (ThrownEntityImpactCallback listener : listeners) {
            InteractionResult result = listener.interact(entity, hitResult);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(ThrowableProjectile entity, HitResult hitResult);
}
