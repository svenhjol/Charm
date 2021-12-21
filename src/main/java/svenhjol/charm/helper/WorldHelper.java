package svenhjol.charm.helper;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.registry.CommonRegistry;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @version 4.0.0-charm
 */
@SuppressWarnings("unused")
public class WorldHelper {
    public static boolean addForcedChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);

        // try a couple of times I guess?
        boolean result = false;
        for (int i = 0; i <= 2; i++) {
            result = level.setChunkForced(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), true);
            if (result) break;
        }

        if (result) {
            LogHelper.debug(WorldHelper.class, "Force loaded chunk " + chunkPos);
        }

        return result;
    }

    public static boolean removeForcedChunk(ServerLevel level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        boolean result = level.setChunkForced(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), false);
        if (!result) {
            LogHelper.error(WorldHelper.class, "Could not unload forced chunk - this is probably really bad.");
        } else {
            LogHelper.debug(WorldHelper.class, "Unloaded forced chunk " + chunkPos);
        }
        return result;
    }

    public static void removeStructures(ServerLevel level, List<StructureFeature<?>> structureFeatures) {
        StructureSettings settings = level.getChunkSource().getGenerator().getSettings();
        Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig = new HashMap<>(settings.structureConfig());

        for (StructureFeature<?> structureFeature : structureFeatures) {
            structureConfig.remove(structureFeature);
        }

        settings.structureConfig = structureConfig;
    }

    public static void clearWeather(ServerLevel level) {
        clearWeather(level, level.random.nextInt(12000) + 3600);
    }

    public static void clearWeather(ServerLevel level, int duration) {
        level.setWeatherParameters(duration, 0, false, false);
    }

    public static void stormyWeather(ServerLevel level) {
        stormyWeather(level, level.random.nextInt(12000) + 3600);
    }

    public static void stormyWeather(ServerLevel level, int duration) {
        level.setWeatherParameters(0, duration, true, true);
    }

    public static PoiType addPointOfInterestType(ResourceLocation id, Block block, int ticketCount) {
        PoiType poit = PointOfInterestTypeAccessor.callCreate(id.toString(), ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates()), ticketCount, 1);
        CommonRegistry.pointOfInterestType(id, poit);
        return PointOfInterestTypeAccessor.callSetup(poit);
    }

    public static void addBlockStatesToPointOfInterest(PoiType poit, List<BlockState> states) {
        Set<BlockState> existingStates = poit.matchingStates;
        if (existingStates instanceof ImmutableSet) {
            List<BlockState> mutable = new ArrayList<>(existingStates);
            mutable.addAll(states);
            poit.matchingStates = ImmutableSet.copyOf(mutable);
        } else {
            existingStates.addAll(states);
            poit.matchingStates = existingStates;
        }

        PoiType.ALL_STATES.addAll(states);
        states.forEach(state -> PoiType.TYPE_BY_STATE.put(state, poit));
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

    public static boolean isInsideStructure(ServerLevel level, BlockPos pos, StructureFeature<?> structure) {
        return level.structureFeatureManager().getStructureAt(pos, structure).isValid();
    }

    public static boolean isLikeSolid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return isSolid(level, pos) || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.PLANT;
    }

    public static boolean isLikeAir(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.canOcclude() || state.getMaterial() == Material.WATER || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.PLANT || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.CLAY;
    }

    public static boolean isSolid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canOcclude() && !level.isEmptyBlock(pos) && !state.getMaterial().isLiquid();
    }

    public static boolean isStructure(ResourceLocation structureId) {
        // TODO: static cache
        return Registry.STRUCTURE_FEATURE.get(structureId) != null;
    }

    public static boolean isBiome(ResourceLocation biomeId) {
        // TODO: static cache
        return BuiltinRegistries.BIOME.get(biomeId) != null;
    }

    @Nullable
    public static BlockPos getSurfacePos(Level level, BlockPos pos) {
        return getSurfacePos(level, pos, level.getMaxBuildHeight());
    }

    @Nullable
    public static BlockPos getSurfacePos(Level level, BlockPos pos, int startAtHeight) {
        int surface = 0;

        for (int y = startAtHeight; y >= 0; --y) {
            BlockPos n = new BlockPos(pos.getX(), y, pos.getZ());
            if (level.isEmptyBlock(n) && !level.isEmptyBlock(n.below())) {
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

    /**
     * Creates an explosion at the target position.
     */
    public static void explode(ServerLevel level, BlockPos pos, float size, Explosion.BlockInteraction interaction) {
        level.explode(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, size, interaction);
    }

    public static void syncBlockEntityToClient(ServerLevel level, BlockPos pos) {
        level.getChunkSource().blockChanged(pos);
    }
}
