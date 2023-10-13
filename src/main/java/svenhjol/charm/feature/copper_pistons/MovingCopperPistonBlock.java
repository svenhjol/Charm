package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.MovingPistonBlock;

public class MovingCopperPistonBlock extends MovingPistonBlock {
    public MovingCopperPistonBlock() {
        super(Properties.copy(Blocks.MOVING_PISTON));
    }
}
