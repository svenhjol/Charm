package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.charmony.common.block.CharmBlock;
import svenhjol.charm.charmony.common.block.CharmBlockItem;

import java.util.function.Supplier;

public class Block extends CharmBlock<EnderPearlBlock> {
    public Block() {
        super(Properties.of()
            .sound(SoundType.GLASS)
            .strength(2.0f));
    }

    /**
     * Copypasta from {@link net.minecraft.world.level.block.MyceliumBlock}
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.PORTAL,
                pos.getX() + random.nextDouble(),
                pos.getY() + 1.1d,
                pos.getZ() + random.nextDouble(), 0.0d, 0.0d, 0.0d);
        }
    }

    @Override
    public Class<EnderPearlBlock> typeForFeature() {
        return EnderPearlBlock.class;
    }

    static class BlockItem extends CharmBlockItem<Block> {
        public BlockItem(Supplier<Block> block) {
            super(block, new Properties());
        }
    }
}
