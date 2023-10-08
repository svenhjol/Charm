package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"deprecation", "BooleanMethodIsAlwaysInverted", "unused"})
public class SugarBlock extends FallingBlock {
    public SugarBlock() {
        super(Properties.of()
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    public static CharmonyFeature getParent() {
        return Charm.instance().loader().get(StorageBlocks.class).orElseThrow();
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
        var waterBelow = false;

        for (var facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                var below = pos.relative(facing);
                if (level.getBlockState(below).is(Blocks.WATER)) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));

            if (BumblezoneIntegration.enabled()) {
                if (BumblezoneIntegration.bumblezoneFluid == null) {
                    BumblezoneIntegration.bumblezoneFluid = BuiltInRegistries.BLOCK.get(BumblezoneIntegration.BUMBLEZONE_FLUID_ID);
                }

                level.setBlock(pos, BumblezoneIntegration.bumblezoneFluid.defaultBlockState(), 3);

                // Find all water blocks in contact recursively. Uses a set since we do not need duplicate positions
                Set<BlockPos> positionsToChange = BumblezoneIntegration.recursiveReplaceWater(level, pos, 0, 3, new HashSet<>());

                // Now change to sugar water after we found all water in range. Prevents weird shapes from being made when we delay this
                positionsToChange.forEach(waterPos -> level.setBlock(waterPos, BumblezoneIntegration.bumblezoneFluid.defaultBlockState(), 3));
            } else {
                level.removeBlock(pos, true);
                level.playSound(null, pos, Sugar.dissolveSound.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            if (!level.isClientSide()) {
                Sugar.triggerDissolvedSugar((ServerLevel) level, pos);
            }
        }

        return waterBelow;
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(getParent(), Sugar.block, new Properties());
        }
    }
}
