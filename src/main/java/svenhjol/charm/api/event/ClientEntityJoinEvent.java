package svenhjol.charm.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class ClientEntityJoinEvent extends CharmEvent<ClientEntityJoinEvent.Handler> {
    public static final ClientEntityJoinEvent INSTANCE = new ClientEntityJoinEvent();

    private ClientEntityJoinEvent() {}

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
