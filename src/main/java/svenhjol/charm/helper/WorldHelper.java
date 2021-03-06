package svenhjol.charm.helper;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.mixin.accessor.PoiTypeAccessor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unused")
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
            LogHelper.debug(WorldHelper.class, "Force loaded chunk " + chunkPos);

        return result;
    }

    public static boolean removeForcedChunk(ServerLevel world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        boolean result = world.setChunkForced(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), false);
        if (!result) {
            LogHelper.error(WorldHelper.class, "Could not unload forced chunk - this is probably really bad.");
        } else {
            LogHelper.debug(WorldHelper.class, "Unloaded forced chunk " + chunkPos);
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

    public static BlockPos addRandomOffset(BlockPos pos, Random rand, int min, int max) {
        int n = rand.nextInt(max - min) + min;
        int e = rand.nextInt(max - min) + min;
        int s = rand.nextInt(max - min) + min;
        int w = rand.nextInt(max - min) + min;
        pos = pos.north(rand.nextBoolean() ? n : -n);
        pos = pos.east(rand.nextBoolean() ? e : -e);
        pos = pos.south(rand.nextBoolean() ? s : -s);
        pos = pos.west(rand.nextBoolean() ? w : -w);
        return pos;
    }

    public static double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static boolean isInsideStructure(ServerLevel world, BlockPos pos, StructureFeature<?> structure) {
        return world.structureFeatureManager().getStructureAt(pos, true, structure).isValid();
    }

    public static boolean isLikeSolid(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return isSolid(world, pos) || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.PLANT;
    }

    public static boolean isLikeAir(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return !state.canOcclude() || state.getMaterial() == Material.WATER || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.PLANT || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.CLAY;
    }

    public static boolean isSolid(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.canOcclude() && !world.isEmptyBlock(pos) && !state.getMaterial().isLiquid();
    }

    @Nullable
    public static BlockPos getSurfacePos(Level world, BlockPos pos) {
        int surface = 0;

        for (int y = world.getMaxBuildHeight(); y >= 0; --y) {
            BlockPos n = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.isEmptyBlock(n) && !world.isEmptyBlock(n.below())) {
                surface = y;
                break;
            }
        }

        if (surface <= 0) {
            LogHelper.warn(WorldHelper.class, "Failed to find a surface value to spawn the player");
            return null;
        }

        return new BlockPos(pos.getX(), surface, pos.getZ());
    }
}
