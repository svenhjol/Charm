package svenhjol.charm.feature.redstone_sand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charmony.base.CharmonyBlockItem;

@SuppressWarnings("deprecation")
public class RedstoneSandBlock extends FallingBlock {
    public RedstoneSandBlock() {
        super(Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(RedstoneSand.block, new Properties());
        }
    }
}
