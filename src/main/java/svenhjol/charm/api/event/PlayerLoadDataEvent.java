package svenhjol.charm.api.event;

import net.minecraft.world.entity.player.Player;

import java.io.File;

public class PlayerLoadDataEvent extends CharmEvent<PlayerLoadDataEvent.Handler> {
    public static final PlayerLoadDataEvent INSTANCE = new PlayerLoadDataEvent();

    private PlayerLoadDataEvent() {}

    public void invoke(Player player, File playerDataDir) {
        for (Handler handler : getHandlers()) {
            handler.run(player, playerDataDir);
        }
    }

    public interface Handler {
        void run(Player player, File playerDataDir);
    }
}
