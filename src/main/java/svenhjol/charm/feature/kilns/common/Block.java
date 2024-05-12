package svenhjol.charm.feature.kilns.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.foundation.feature.FeatureResolver;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Block extends AbstractFurnaceBlock implements FeatureResolver<Kilns> {
    private static final MapCodec<Block> CODEC = simpleCodec(Block::new);

    public Block() {
        this(Properties.of()
            .strength(3.5f)
            .lightLevel(l -> l.getValue(BlockStateProperties.LIT) ? 13 : 0));
    }

    private Block(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, feature().registers.blockEntity.get());
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntity kiln) {
            player.openMenu(kiln);
        }
    }

    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntity(pos, state);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            var x = pos.getX() + 0.5d;
            var y = pos.getY();
            var z = pos.getZ() + 0.5d;

            if (random.nextDouble() < 0.1d) {
                level.playLocalSound(x, y, z, feature().registers.bakeSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }

            level.addParticle(ParticleTypes.SMOKE, x, y + 1.0d, z, 0.0d, 0.0d, 0.0d);
        }
    }

    @Override
    public Class<Kilns> typeForFeature() {
        return Kilns.class;
    }

    static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<Block> block) {
            super(block.get(), new Properties());
        }
    }
}
