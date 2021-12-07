package svenhjol.charm.module.block_of_sugar;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.block.CharmFallingBlock;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.bumblezone_integration.BumblezoneIntegration;

import java.util.HashSet;
import java.util.Set;

public class SugarBlock extends CharmFallingBlock {
    public SugarBlock(CharmModule module) {
        super(module, "sugar_block", FabricBlockSettings
            .of(Material.SAND)
            .sounds(SoundType.SAND)
            .strength(0.5F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(Level level, BlockPos pos, BlockState state) {
        boolean waterBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.relative(facing);
                if (level.getBlockState(below).getMaterial() == Material.WATER) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));

            if (Charm.LOADER.isEnabled(BumblezoneIntegration.class)) {
                if (BumblezoneIntegration.bumblezoneFluid == null) {
                    BumblezoneIntegration.bumblezoneFluid = Registry.BLOCK.get(BumblezoneIntegration.BUMBLEZONE_FLUID_ID);
                }

                level.setBlock(pos, BumblezoneIntegration.bumblezoneFluid.defaultBlockState(), 3);

                // Find all water blocks in contact recursively. Uses a set since we do not need duplicate positions
                Set<BlockPos> positionsToChange = BumblezoneIntegration.recursiveReplaceWater(level, pos, 0, 3, new HashSet<>());

                // Now change to sugar water after we found all water in range. Prevents weird shapes from being made when we delay this
                positionsToChange.forEach(waterPos -> level.setBlock(waterPos, BumblezoneIntegration.bumblezoneFluid.defaultBlockState(), 3));
            } else {
                level.removeBlock(pos, true);
                level.playSound(null, pos, BlockOfSugar.SUGAR_DISSOLVE_SOUND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            if (!level.isClientSide) {
                BlockOfSugar.triggerAdvancementForNearbyPlayers((ServerLevel) level, pos);
            }
        }

        return waterBelow;
    }
}
