package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;

import java.io.File;

@SuppressWarnings("unused")
public interface PlayerSaveDataCallback {
    Event<PlayerSaveDataCallback> EVENT = EventFactory.createArrayBacked(PlayerSaveDataCallback.class, (listeners) -> (player, dataDir) -> {
        for (PlayerSaveDataCallback listener : listeners) {
            listener.interact(player, dataDir);
        }
    });

    static void writeFile(File file, CompoundTag nbt) {
        try {
            NbtIo.writeCompressed(nbt, file);
        } catch (Exception e) {
            LogManager.getLogger().warn("Failed to save player data to file: " + file.toString());
        }
    }

    void interact(Player player, File playerDataDir);
}
