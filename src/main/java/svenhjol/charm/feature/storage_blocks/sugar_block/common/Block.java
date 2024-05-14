package svenhjol.charm.feature.storage_blocks.sugar_block.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.event.SugarDissolveEvent;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlock;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.function.Supplier;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public class Block extends FallingBlock implements FeatureResolver<SugarBlock> {
    static final MapCodec<Block> CODEC = simpleCodec(Block::new);

    public Block() {
        this(Properties.of()
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    private Block(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving) {
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
            level.globalLevelEvent(2001, pos, net.minecraft.world.level.block.Block.getId(level.getBlockState(pos)));

            SugarDissolveEvent.INSTANCE.invoke(level, pos);

            if (!level.isClientSide()) {
                feature().advancements.dissolvedSugar((ServerLevel) level, pos);
            }
        }

        return waterBelow;
    }

    @Override
    public Class<SugarBlock> typeForFeature() {
        return SugarBlock.class;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<Block> block) {
            super(block.get(), new Properties());
        }
    }
}
