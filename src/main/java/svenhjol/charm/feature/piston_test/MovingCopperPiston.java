package svenhjol.charm.feature.piston_test;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.MovingPistonBlock;

public class MovingCopperPiston extends MovingPistonBlock {
    public MovingCopperPiston() {
        super(Properties.copy(Blocks.MOVING_PISTON));
    }
}
