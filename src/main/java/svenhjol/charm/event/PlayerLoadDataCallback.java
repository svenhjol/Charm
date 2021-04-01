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
        NbtCompound tag;

        if (file.exists()) {
            try {
                tag = NbtIo.readCompressed(file);
            } catch (Exception e) {
                Charm.LOG.error("Failed to load player data from file: " + file.toString());
                tag = new NbtCompound();
            }
        } else {
            tag = new NbtCompound();
        }

        return tag;
    }

    void interact(PlayerEntity player, File playerDataDir);
}
