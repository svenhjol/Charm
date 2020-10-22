package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import svenhjol.charm.Charm;

import java.io.File;

public interface PlayerSaveDataCallback {
    Event<PlayerSaveDataCallback> EVENT = EventFactory.createArrayBacked(PlayerSaveDataCallback.class, (listeners) -> (player, dataDir) -> {
        for (PlayerSaveDataCallback listener : listeners) {
            listener.interact(player, dataDir);
        }
    });

    static void writeFile(File file, CompoundTag tag) {
        try {
            NbtIo.writeCompressed(tag, file);
        } catch (Exception e) {
            Charm.LOG.error("Failed to save player data to file: " + file.toString());
        }
    }

    void interact(PlayerEntity player, File playerDataDir);
}
