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
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Increases snow layers when snowing in cold biomes")
public class SnowAccumulation extends CharmModule {
    // snow won't be accumulated on any of these blocks
    public final static List<Block> BLOCK_BLACKLIST;

    @Config(name = "Allow block replace", description = "If true, placing a block on a non-full snow block will replace the snow block.\n" +
        "If false, use vanilla behavior (only a snow block with a single layer will allow block replacement).")
    public static boolean allowBlockReplace = true;

    @Config(name = "Powder snow chance", description = "Chance (out of 1.0) of a fully accumulated snow block being converted to powder snow.")
    public static double convertToPowderSnow = 0.01D;

    public static boolean shouldAccumulateSnow(ServerLevel level) {
        return Charm.LOADER.isEnabled(SnowAccumulation.class) && level.isRaining();
    }

    public static void tryPlaceSnow(ServerLevel level, int chunkX, int chunkZ) {
        if (!shouldAccumulateSnow(level)) return;

        if (level.random.nextDouble() < 0.01D) {
            BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(chunkX, 0, chunkZ, 15));
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            Biome biome = level.getBiome(pos);

            if (biome.getHeightAdjustedTemperature(pos) < 0.15F && pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight()) {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                Block belowBlock = belowState.getBlock();

                if (state.isAir()) {
                    // Allow placement of snow on any solid surface that isn't in the blacklist.
                    if (!BLOCK_BLACKLIST.contains(belowBlock) && Block.isFaceFull(belowState.getCollisionShape(level, belowPos), Direction.UP)) {
                        level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                        return;
                    }
                }

                if (block == Blocks.SNOW) {

                    if (belowState.getBlock() == Blocks.SNOW_BLOCK || belowState.getBlock() == Blocks.POWDER_SNOW) {
                        // Don't build snow layers forever.
                        return;
                    }

                    int layers = state.getValue(SnowLayerBlock.LAYERS);

                    if (layers < 8) {
                        state = state.setValue(SnowLayerBlock.LAYERS, ++layers);
                    } else {
                        if (level.random.nextDouble() < convertToPowderSnow) {
                            state = Blocks.POWDER_SNOW.defaultBlockState();
                        } else {
                            state = Blocks.SNOW_BLOCK.defaultBlockState();
                        }
                    }
                    level.setBlockAndUpdate(pos, state);
                }
            }
        }
    }

    static {
        BLOCK_BLACKLIST = Arrays.asList(
            Blocks.ICE,
            Blocks.PACKED_ICE,
            Blocks.BARRIER,
            Blocks.HONEY_BLOCK,
            Blocks.SOUL_SAND,
            Blocks.SNOW,
            Blocks.SNOW_BLOCK,
            Blocks.POWDER_SNOW
        );
    }
}
