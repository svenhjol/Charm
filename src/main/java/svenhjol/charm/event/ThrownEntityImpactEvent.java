package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;

public interface ThrownEntityImpactEvent {
    Event<ThrownEntityImpactEvent> EVENT = EventFactory.createArrayBacked(ThrownEntityImpactEvent.class, (listeners) -> (entity, hitResult) -> {
        for (ThrownEntityImpactEvent listener : listeners) {
            InteractionResult result = listener.interact(entity, hitResult);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(ThrowableProjectile entity, HitResult hitResult);
}
