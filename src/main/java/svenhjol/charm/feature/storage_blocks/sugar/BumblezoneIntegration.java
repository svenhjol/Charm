package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.helper.ConfigHelper;

import java.util.HashSet;
import java.util.Set;

public class BumblezoneIntegration {
    public static final ResourceLocation BUMBLEZONE_FLUID_ID = new ResourceLocation("the_bumblezone", "sugar_water_block");
    public static Block bumblezoneFluid = null;

    public static boolean enabled() {
        return ConfigHelper.isModLoaded("the_bumblezone");
    }

    /**
     * Will recursively track down and replace touching water blocks with bumblezone fluid.
     * Do not set the maxDepth too high!
     * @return - waterPos
     */
    public static Set<BlockPos> recursiveReplaceWater(Level level, BlockPos pos, int depth, int maxDepth, HashSet<BlockPos> waterPos){
        // exit when we hit as far as we wanted
        if (depth == maxDepth) {
            return waterPos;
        }

        // Find the touching water blocks, replace them, and call this method on those blocks
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (var facing : Direction.values()) {
            neighborPos.set(pos).move(facing);
            var neighborBlock = level.getBlockState(neighborPos);

            // Found watery block to replace, store the position of the water
            if (!neighborBlock.getBlock().equals(bumblezoneFluid)
                && neighborBlock.is(Blocks.WATER)
                && neighborBlock.getFluidState().isSource())
            {
                waterPos.add(neighborPos.immutable());
                recursiveReplaceWater(level, neighborPos, depth + 1, maxDepth, waterPos);
            }
        }

        return waterPos;
    }
}
