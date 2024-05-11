package svenhjol.charm.feature.copper_pistons.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import java.util.function.Supplier;

public class CopperPistonBaseBlock extends PistonBaseBlock {
    public CopperPistonBaseBlock() {
        super(false, Properties.ofFullCopy(Blocks.PISTON)
            .isRedstoneConductor((state, get, pos) -> false));
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Properties());
        }
    }
}
