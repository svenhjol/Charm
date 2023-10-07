package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

public class StickyCopperPistonBaseBlock extends PistonBaseBlock {
    public StickyCopperPistonBaseBlock() {
        super(true, Properties.copy(Blocks.STICKY_PISTON)
            .isRedstoneConductor((state, get, pos) -> false));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public <T extends Block> BlockItem(CharmonyFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
