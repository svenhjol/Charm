package svenhjol.charm.feature.piston_test;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonHeadBlock;

public class CopperPistonHeadBlock extends PistonHeadBlock {
    public CopperPistonHeadBlock() {
        super(Properties.copy(Blocks.PISTON_HEAD));
    }
}
