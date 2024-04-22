package svenhjol.charm.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class ClientEntityLeaveEvent extends CharmEvent<ClientEntityLeaveEvent.Handler> {
    public static final ClientEntityLeaveEvent INSTANCE = new ClientEntityLeaveEvent();

    private ClientEntityLeaveEvent() {}

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
