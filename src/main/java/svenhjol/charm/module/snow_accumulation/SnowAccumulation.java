package svenhjol.charm.module.snow_accumulation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Increases snow layers when snowing in cold biomes")
public class SnowAccumulation extends CharmModule {
    // snow won't be accumulated on any of these blockstates
    public final static List<BlockState> STATE_BLACKLIST;

    public static boolean shouldAccumulateSnow() {
        return Charm.LOADER.isEnabled(SnowAccumulation.class);
    }

    public static void tryPlaceSnow(ServerLevel level, int chunkX, int chunkZ) {
        if (!shouldAccumulateSnow() || !level.isRaining()) return;

        if (level.random.nextDouble() < 0.015D) {
            BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(chunkX, 0, chunkZ, 15));
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            Biome biome = level.getBiome(pos);

            if (biome.getHeightAdjustedTemperature(pos) < 0.15F && pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                if (state.isAir()) {
                    if (!STATE_BLACKLIST.contains(belowState) && Block.isFaceFull(belowState.getCollisionShape(level, belowPos), Direction.UP)) {
                        level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                        return;
                    }
                }

                if (block == Blocks.SNOW) {
                    int layers = state.getValue(SnowLayerBlock.LAYERS);
                    if (layers < 8) {
                        state = state.setValue(SnowLayerBlock.LAYERS, ++layers);
                        level.setBlockAndUpdate(pos, state);
                    }
                }
            }
        }
    }

    static {
        STATE_BLACKLIST = Arrays.asList(
            Blocks.ICE.defaultBlockState(),
            Blocks.PACKED_ICE.defaultBlockState(),
            Blocks.BARRIER.defaultBlockState(),
            Blocks.HONEY_BLOCK.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
            Blocks.SNOW.defaultBlockState()
        );
    }
}
