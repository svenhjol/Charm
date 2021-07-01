package svenhjol.charm.module.bumblezone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CharmCommonModule;

import java.util.HashSet;
import java.util.Set;

@CommonModule(mod = Charm.MOD_ID, description = "Bumblezone integration.")
public class Bumblezone extends CharmCommonModule {
    public static final ResourceLocation BUMBLEZONE_FLUID_ID = new ResourceLocation("the_bumblezone", "sugar_water_block");
    public static Block bumblezoneFluid = null;

    @Override
    public void register() {
        this.addDependencyCheck(module -> ModHelper.isLoaded("bumblezone"));
    }

    /**
     * Will recursively track down and replace touching water blocks with bumblezone fluid.
     * Do not set the maxDepth too high!
     * @return - waterPos
     */
    public static Set<BlockPos> recursiveReplaceWater(Level world, BlockPos position, int depth, int maxDepth, HashSet<BlockPos> waterPos){
        // exit when we hit as far as we wanted
        if(depth == maxDepth) return waterPos;

        // Find the touching water blocks, replace them, and call this method on those blocks
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction facing : Direction.values()) {
            neighborPos.set(position).move(facing);
            BlockState neighborBlock = world.getBlockState(neighborPos);

            // Found watery block to replace, store the position of the water
            if (!neighborBlock.getBlock().equals(bumblezoneFluid) && neighborBlock.getMaterial() == Material.WATER && neighborBlock.getFluidState().isSource()) {
                waterPos.add(neighborPos.immutable());
                recursiveReplaceWater(world, neighborPos, depth + 1, maxDepth, waterPos);
            }
        }

        return waterPos;
    }
}
