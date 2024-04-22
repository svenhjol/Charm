package svenhjol.charm.api.event;

import net.minecraft.world.entity.player.Player;

import java.io.File;

public class PlayerSaveDataEvent extends CharmEvent<PlayerSaveDataEvent.Handler> {
    public static final PlayerSaveDataEvent INSTANCE = new PlayerSaveDataEvent();

    private PlayerSaveDataEvent() {}

    public void invoke(Player player, File playerDataDir) {
        for (Handler handler : getHandlers()) {
            handler.run(player, playerDataDir);
        }
    }

    public interface Handler {
        void run(Player player, File playerDataDir);
    }
}
