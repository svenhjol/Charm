package svenhjol.charm.base.helper;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.RegistryHandler;

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
            Charm.LOG.debug("Force loaded chunk " + chunkPos.toString());

        return result;
    }

    public static boolean removeForcedChunk(ServerWorld world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        boolean result = world.setChunkForced(chunkPos.getStartX(), chunkPos.getStartZ(), false);
        if (!result) {
            Charm.LOG.error("Could not unload forced chunk - this is probably really bad.");
        } else {
            Charm.LOG.debug("Unloaded forced chunk " + chunkPos.toString());
        }
        return result;
    }

    public static void clearWeather(ServerWorld world) {
        clearWeather(world, world.random.nextInt(12000) + 3600);
    }

    public static void clearWeather(ServerWorld world, int duration) {
        world.setWeather(duration, 0, false, false);
    }

    public static void stormyWeather(ServerWorld world) {
        stormyWeather(world, world.random.nextInt(12000) + 3600);
    }

    public static void stormyWeather(ServerWorld world, int duration) {
        world.setWeather(0, duration, true, true);
    }

    public static PointOfInterestType addPointOfInterestType(Identifier id, Block block, int ticketCount) {
        PointOfInterestType poit = PointOfInterestTypeAccessor.callCreate(id.toString(), ImmutableSet.copyOf(block.getStateManager().getStates()), ticketCount, 1);
        RegistryHandler.pointOfInterestType(id, poit);
        return PointOfInterestTypeAccessor.callSetup(poit);
    }
}
