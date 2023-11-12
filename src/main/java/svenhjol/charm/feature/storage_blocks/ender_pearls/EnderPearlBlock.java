package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;

public class EnderPearlBlock extends CharmonyBlock {
    public EnderPearlBlock() {
        super(Properties.of()
            .sound(SoundType.GLASS)
            .strength(2.0F));
    }

    /**
     * Copypasta from {@link net.minecraft.world.level.block.MyceliumBlock}
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.PORTAL, pos.getX() + random.nextDouble(), pos.getY() + 1.1D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(EnderPearls.block, new Properties());
        }
    }
}
