package svenhjol.charm.module.bumblezone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.annotation.Module;

import java.util.HashSet;
import java.util.Set;

@Module(mod = Charm.MOD_ID, description = "Bumblezone integration.")
public class Bumblezone extends CharmModule {
    public static final Identifier BUMBLEZONE_FLUID_ID = new Identifier("the_bumblezone", "sugar_water_block");
    public static Block bumblezoneFluid = null;

    @Override
    public boolean depends() {
        return ModHelper.isLoaded("bumblezone");
    }

    /**
     * Will recursively track down and replace touching water blocks with bumblezone fluid.
     * Do not set the maxDepth too high!
     * @return - waterPos
     */
    public static Set<BlockPos> recursiveReplaceWater(World world, BlockPos position, int depth, int maxDepth, HashSet<BlockPos> waterPos){
        // exit when we hit as far as we wanted
        if(depth == maxDepth) return waterPos;

        // Find the touching water blocks, replace them, and call this method on those blocks
        BlockPos.Mutable neighborPos = new BlockPos.Mutable();
        for (Direction facing : Direction.values()) {
            neighborPos.set(position).move(facing);
            BlockState neighborBlock = world.getBlockState(neighborPos);

            // Found watery block to replace, store the position of the water
            if (!neighborBlock.getBlock().equals(bumblezoneFluid) && neighborBlock.getMaterial() == Material.WATER && neighborBlock.getFluidState().isStill()) {
                waterPos.add(neighborPos.toImmutable());
                recursiveReplaceWater(world, neighborPos, depth + 1, maxDepth, waterPos);
            }
        }

        return waterPos;
    }
}
