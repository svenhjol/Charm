package svenhjol.charm.helper;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.PoiTypeAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"unused"})
public class WorldHelper {
    public static boolean addForcedChunk(ServerLevel world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);

        // try a couple of times I guess?
        boolean result = false;
        for (int i = 0; i <= 2; i++) {
            result = world.setChunkForced(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), true);
            if (result) break;
        }
        if (result)
            Charm.LOG.debug("[WorldHelper] Force loaded chunk " + chunkPos);

        return result;
    }

    public static boolean removeForcedChunk(ServerLevel world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        boolean result = world.setChunkForced(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), false);
        if (!result) {
            Charm.LOG.error("[WorldHelper] Could not unload forced chunk - this is probably really bad.");
        } else {
            Charm.LOG.debug("[WorldHelper] Unloaded forced chunk " + chunkPos);
        }
        return result;
    }

    public static void clearWeather(ServerLevel world) {
        clearWeather(world, world.random.nextInt(12000) + 3600);
    }

    public static void clearWeather(ServerLevel world, int duration) {
        world.setWeatherParameters(duration, 0, false, false);
    }

    public static void stormyWeather(ServerLevel world) {
        stormyWeather(world, world.random.nextInt(12000) + 3600);
    }

    public static void stormyWeather(ServerLevel world, int duration) {
        world.setWeatherParameters(0, duration, true, true);
    }

    public static PoiType addPointOfInterestType(ResourceLocation id, Block block, int ticketCount) {
        PoiType poit = PointOfInterestTypeAccessor.callCreate(id.toString(), ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()), ticketCount, 1);
        RegistryHelper.pointOfInterestType(id, poit);
        return PointOfInterestTypeAccessor.callSetup(poit);
    }

    public static void addBlockStatesToPointOfInterest(PoiType poit, List<BlockState> states) {
        // we need to wrap the poit with charm's accessor so that we can get and set blockstates
        PoiTypeAccessor wrappedPoit = (PoiTypeAccessor)poit;

        Set<BlockState> existingStates = wrappedPoit.getMatchingStates();
        if (existingStates instanceof ImmutableSet) {
            List<BlockState> mutable = new ArrayList<>(existingStates);
            mutable.addAll(states);
            wrappedPoit.setMatchingStates(ImmutableSet.copyOf(mutable));
        } else {
            existingStates.addAll(states);
            wrappedPoit.setMatchingStates(existingStates);
        }

        PoiTypeAccessor.getAllStates().addAll(states);
        states.forEach(state -> PoiTypeAccessor.getTypeByState().put(state, poit));
    }
}
