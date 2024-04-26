package svenhjol.charm.feature.variant_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Tags;

public class CommonCallbacks {
    static final Log LOGGER = new Log(Charm.ID, "VariantPistons");

    public static boolean alsoCheckTags(BlockState state, Block block) {
        var defaultState = block.defaultBlockState();
        boolean found = false;

        if (defaultState.is(Blocks.MOVING_PISTON)) {
            LOGGER.debug("found in moving_pistons tag: " + block);
            found = state.is(Tags.MOVING_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON)) {
            LOGGER.debug("found in pistons tag: " + block);
            found = state.is(Tags.PISTONS);
        }
        if (defaultState.is(Blocks.STICKY_PISTON)) {
            LOGGER.debug("found in sticky_pistons tag: " + block);
            found = state.is(Tags.STICKY_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON_HEAD)) {
            LOGGER.debug("found in piston_heads tag: " + block);
            found = state.is(Tags.PISTON_HEADS);
        }

        if (found) return true;

        // Fallthrough to default behavior
        return state.is(block);
    }
}
