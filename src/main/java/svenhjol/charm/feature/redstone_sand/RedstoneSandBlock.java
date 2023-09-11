package svenhjol.charm.feature.redstone_sand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;

@SuppressWarnings("deprecation")
public class RedstoneSandBlock extends FallingBlock {
    public static CharmFeature getParent() {
        return Charm.instance().loader().get(RedstoneSand.class).orElseThrow();
    }

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

    static class BlockItem extends CharmBlockItem {
        public BlockItem() {
            super(getParent(), RedstoneSand.block, new Properties());
        }
    }
}
