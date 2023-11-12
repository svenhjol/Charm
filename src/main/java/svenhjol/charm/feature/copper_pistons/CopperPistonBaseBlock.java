package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import svenhjol.charmony.base.CharmonyBlockItem;

import java.util.function.Supplier;

public class CopperPistonBaseBlock extends PistonBaseBlock {
    public CopperPistonBaseBlock() {
        super(false, Properties.copy(Blocks.PISTON)
            .isRedstoneConductor((state, get, pos) -> false));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block, new Properties());
        }
    }
}
