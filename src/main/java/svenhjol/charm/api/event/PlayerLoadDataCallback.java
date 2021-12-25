package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;

import java.io.File;

@SuppressWarnings("unused")
public interface PlayerLoadDataCallback {
    Event<PlayerLoadDataCallback> EVENT = EventFactory.createArrayBacked(PlayerLoadDataCallback.class, (listeners) -> (player, dataDir) -> {
        for (PlayerLoadDataCallback listener : listeners) {
            listener.interact(player, dataDir);
        }
    });

    static CompoundTag readFile(File file) {
        CompoundTag tag;

        if (file.exists()) {
            try {
                tag = NbtIo.readCompressed(file);
            } catch (Exception e) {
                LogManager.getLogger().warn("Failed to load player data from file: " + file);
                tag = new CompoundTag();
            }
        } else {
            tag = new CompoundTag();
        }

        return tag;
    }

    void interact(Player player, File playerDataDir);
}
