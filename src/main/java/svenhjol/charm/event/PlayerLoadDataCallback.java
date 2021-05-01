package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import svenhjol.charm.Charm;

import java.io.File;

public interface PlayerLoadDataCallback {
    Event<PlayerLoadDataCallback> EVENT = EventFactory.createArrayBacked(PlayerLoadDataCallback.class, (listeners) -> (player, dataDir) -> {
        for (PlayerLoadDataCallback listener : listeners) {
            listener.interact(player, dataDir);
        }
    });

    static NbtCompound readFile(File file) {
        NbtCompound nbt;

        if (file.exists()) {
            try {
                nbt = NbtIo.readCompressed(file);
            } catch (Exception e) {
                Charm.LOG.error("Failed to load player data from file: " + file.toString());
                nbt = new NbtCompound();
            }
        } else {
            nbt = new NbtCompound();
        }

        return nbt;
    }

    void interact(PlayerEntity player, File playerDataDir);
}
