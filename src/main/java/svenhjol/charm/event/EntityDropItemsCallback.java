package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface EntityDropItemsCallback {
    Event<EntityDropItemsCallback> AFTER = EventFactory.createArrayBacked(EntityDropItemsCallback.class, (listeners) -> (entity, source, lootingLevel) -> {
        for (EntityDropItemsCallback listener : listeners) {
            InteractionResult result = listener.interact(entity, source, lootingLevel);
            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    });

    InteractionResult interact(LivingEntity entity, DamageSource source, int lootingLevel);
}
