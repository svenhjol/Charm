package svenhjol.meson.helper;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import svenhjol.meson.Meson;

public class WorldHelper {
    public static boolean addForcedChunk(ServerWorld world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);

        // try a couple of times I guess?
        boolean result = false;
        for (int i = 0; i <= 2; i++) {
            result = world.setChunkForced(chunkPos.getStartX(), chunkPos.getStartZ(), true);
            if (result) break;
        }
        if (result)
            Meson.LOG.debug("Force loaded chunk " + chunkPos.toString());

        return result;
    }

    public static boolean removeForcedChunk(ServerWorld world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        boolean result = world.setChunkForced(chunkPos.getStartX(), chunkPos.getStartZ(), false);
        if (!result) {
            Meson.LOG.error("Could not unload forced chunk - this is probably really bad.");
        } else {
            Meson.LOG.debug("Unloaded forced chunk " + chunkPos.toString());
        }
        return result;
    }
}
