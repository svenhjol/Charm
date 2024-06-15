package svenhjol.charm.feature.copper_pistons.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import java.util.function.Supplier;

public class StickyCopperPistonBaseBlock extends PistonBaseBlock {
    public StickyCopperPistonBaseBlock() {
        super(true, Properties.copy(Blocks.STICKY_PISTON)
            .isRedstoneConductor((state, get, pos) -> false));
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Properties());
        }
    }
}
