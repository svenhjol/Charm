package svenhjol.charm.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class EntityLeaveEvent extends CharmEvent<EntityLeaveEvent.Handler> {
    public static final EntityLeaveEvent INSTANCE = new EntityLeaveEvent();

    private EntityLeaveEvent() {}

    public void invoke(Entity entity, Level level) {
        for (var handler : getHandlers()) {
            handler.run(entity, level);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Entity entity, Level level);
    }
}
