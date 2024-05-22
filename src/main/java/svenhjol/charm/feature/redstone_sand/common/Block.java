package svenhjol.charm.feature.redstone_sand.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charm.feature.redstone_sand.RedstoneSand;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.function.Supplier;

public class Block extends FallingBlock implements FeatureResolver<RedstoneSand> {
    static final MapCodec<Block> CODEC = simpleCodec(Block::new);

    public Block() {
        this(Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .sound(SoundType.SAND)
            .strength(0.5f));
    }

    private Block(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }

    @Override
    public Class<RedstoneSand> typeForFeature() {
        return RedstoneSand.class;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<Block> block) {
            super(block.get(), new Properties());
        }
    }
}
