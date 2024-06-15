package svenhjol.charm.feature.storage_blocks.gunpowder_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.charmony.feature.FeatureResolver;

import java.util.function.Supplier;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public class Block extends FallingBlock implements FeatureResolver<GunpowderBlock> {
    public Block() {
        this(Properties.of()
            .sound(SoundType.SAND)
            .strength(0.5f));
    }

    private Block(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(Level level, BlockPos pos, BlockState state) {
        var lavaBelow = false;

        for (var facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                var below = pos.relative(facing);
                if (level.getBlockState(below).is(Blocks.LAVA)) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            level.globalLevelEvent(2001, pos, net.minecraft.world.level.block.Block.getId(level.getBlockState(pos)));
            level.removeBlock(pos, true);
            level.playSound(null, pos, feature().registers.dissolveSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

            if (!level.isClientSide()) {
                feature().advancements.dissolvedGunpowder((ServerLevel)level, pos);
            }
        }

        return lavaBelow;
    }

    @Override
    public Class<GunpowderBlock> typeForFeature() {
        return GunpowderBlock.class;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<Block> block) {
            super(block.get(), new Properties());
        }
    }
}
