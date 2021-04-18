package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Bumblezone integration.")
public class Bumblezone extends CharmModule {
    public static final Identifier BUMBLEZONE_FLUID_ID = new Identifier("the_bumblezone", "sugar_water_block");
    public static Block bumblezoneFluid = null;

    /**
     * Will recursively track down and replace touching water blocks with bumblezone fluid.
     * Do not set the maxDepth too high!
     */
    public static void recursiveReplaceWater(World world, BlockPos position, int depth, int maxDepth){
        // exit when we hit as far as we wanted
        if(depth == maxDepth) return;

        // Find the touching water blocks, replace them, and call this method on those blocks
        BlockPos.Mutable neighborPos = new BlockPos.Mutable();
        for (Direction facing : Direction.values()) {
            neighborPos.set(position).move(facing);
            BlockState neighborBlock = world.getBlockState(neighborPos);

            // Found watery block to replace, replace and recurse
            if (!neighborBlock.getBlock().is(bumblezoneFluid) && neighborBlock.getMaterial() == Material.WATER) {
                world.setBlockState(neighborPos, bumblezoneFluid.getDefaultState(), 3);
                recursiveReplaceWater(world, position, depth + 1, maxDepth);
            }
        }
    }
}
