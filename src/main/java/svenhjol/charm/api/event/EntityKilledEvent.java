package svenhjol.charm.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class EntityKilledEvent extends CharmEvent<EntityKilledEvent.Handler> {
    public static final EntityKilledEvent INSTANCE = new EntityKilledEvent();

    private EntityKilledEvent() {}

    public void invoke(LivingEntity entity, DamageSource source) {
        for (var handler : getHandlers()) {
            handler.run(entity, source);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(LivingEntity entity, DamageSource source);
    }
}
