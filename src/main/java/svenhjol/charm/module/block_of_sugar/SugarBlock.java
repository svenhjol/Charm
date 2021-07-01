package svenhjol.charm.module.block_of_sugar;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.block.CharmFallingBlock;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.module.bumblezone.Bumblezone;

import java.util.HashSet;
import java.util.Set;

public class SugarBlock extends CharmFallingBlock {
    public SugarBlock(CommonModule module) {
        super(module, "sugar_block", FabricBlockSettings
            .of(Material.SAND)
            .sounds(SoundType.SAND)
            .breakByTool(FabricToolTags.SHOVELS)
            .strength(0.5F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.onPlace(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(Level world, BlockPos pos, BlockState state) {
        boolean waterBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.relative(facing);
                if (world.getBlockState(below).getMaterial() == Material.WATER) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            world.globalLevelEvent(2001, pos, Block.getId(world.getBlockState(pos)));

            if (ModHelper.isLoaded("bumblezone") && Charm.LOADER.isEnabled(Bumblezone.class)) {
                if (Bumblezone.bumblezoneFluid == null) {
                    Bumblezone.bumblezoneFluid = Registry.BLOCK.get(Bumblezone.BUMBLEZONE_FLUID_ID);
                }

                world.setBlock(pos, Bumblezone.bumblezoneFluid.defaultBlockState(), 3);

                // Find all water blocks in contact recursively. Uses a set since we do not need duplicate positions
                Set<BlockPos> positionsToChange = Bumblezone.recursiveReplaceWater(world, pos, 0, 3, new HashSet<>());

                // Now change to sugar water after we found all water in range. Prevents weird shapes from being made when we delay this
                positionsToChange.forEach(waterPos -> world.setBlock(waterPos, Bumblezone.bumblezoneFluid.defaultBlockState(), 3));
            } else {
                world.removeBlock(pos, true);
            }

            if (!world.isClientSide)
                BlockOfSugar.triggerAdvancementForNearbyPlayers((ServerLevel) world, pos);
        }

        return waterBelow;
    }
}
