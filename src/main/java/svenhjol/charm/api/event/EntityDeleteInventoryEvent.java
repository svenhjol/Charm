package svenhjol.charm.api.event;

import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unused")
public class EntityDeleteInventoryEvent extends CharmEvent<EntityDeleteInventoryEvent.Handler> {
    public static final EntityDeleteInventoryEvent INSTANCE = new EntityDeleteInventoryEvent();

    private EntityDeleteInventoryEvent() {}

    public void invoke(LivingEntity entity) {
        for (var handler : getHandlers()) {
            handler.run(entity);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(LivingEntity entity);
    }
}
