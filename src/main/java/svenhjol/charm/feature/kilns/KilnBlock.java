package svenhjol.charm.feature.kilns;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.common.CommonFeature;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class KilnBlock extends AbstractFurnaceBlock {
    protected CommonFeature feature;

    public KilnBlock(CommonFeature feature) {
        super(Properties.of()
            .strength(3.5F)
            .lightLevel(l -> l.getValue(BlockStateProperties.LIT) ? 13 : 0));

        this.feature = feature;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!feature.isEnabled()) {
            return InteractionResult.FAIL;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Kilns.blockEntity.get());
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof KilnBlockEntity kiln) {
            player.openMenu(kiln);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KilnBlockEntity(pos, state);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (state.getValue(LIT)) {
            var x = pos.getX() + 0.5D;
            var y = pos.getY();
            var z = pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, Kilns.bakeSound.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            level.addParticle(ParticleTypes.SMOKE, x, y + 1.1D, z, 0.0D, 0.0D, 0.0D);
        }
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem(Supplier<KilnBlock> block) {
            super(block, new Properties());
        }
    }
}
