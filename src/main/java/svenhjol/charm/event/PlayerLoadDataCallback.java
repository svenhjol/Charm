package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.helper.LogHelper;

import java.io.File;

@SuppressWarnings("unused")
public interface PlayerLoadDataCallback {
    Event<PlayerLoadDataCallback> EVENT = EventFactory.createArrayBacked(PlayerLoadDataCallback.class, (listeners) -> (player, dataDir) -> {
        for (PlayerLoadDataCallback listener : listeners) {
            listener.interact(player, dataDir);
        }
    });

    static CompoundTag readFile(File file) {
        CompoundTag nbt;

        if (file.exists()) {
            try {
                nbt = NbtIo.readCompressed(file);
            } catch (Exception e) {
                LogHelper.error(PlayerLoadDataCallback.class, "Failed to load player data from file: " + file);
                nbt = new CompoundTag();
            }
        } else {
            nbt = new CompoundTag();
        }

        return nbt;
    }

    void interact(Player player, File playerDataDir);
}
