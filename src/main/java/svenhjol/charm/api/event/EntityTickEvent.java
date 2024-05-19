package svenhjol.charm.api.event;

import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unused")
public class EntityTickEvent extends CharmEvent<EntityTickEvent.Handler> {
    public static final EntityTickEvent INSTANCE = new EntityTickEvent();

    private EntityTickEvent() {}

    public void invoke(LivingEntity player) {
        for (var handler : getHandlers()) {
            handler.run(player);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(LivingEntity player);
    }
}
