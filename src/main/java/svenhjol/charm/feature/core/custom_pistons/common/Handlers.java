package svenhjol.charm.feature.core.custom_pistons.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<CustomPistons> {
    public Handlers(CustomPistons feature) {
        super(feature);
    }

    public boolean alsoCheckTags(BlockState state, Block block) {
        var defaultState = block.defaultBlockState();
        boolean found = false;

        if (defaultState.is(Blocks.MOVING_PISTON)) {
            feature().log().dev("found in moving_pistons tag: " + block);
            found = state.is(Tags.MOVING_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON)) {
            feature().log().dev("found in pistons tag: " + block);
            found = state.is(Tags.PISTONS);
        }
        if (defaultState.is(Blocks.STICKY_PISTON)) {
            feature().log().dev("found in sticky_pistons tag: " + block);
            found = state.is(Tags.STICKY_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON_HEAD)) {
            feature().log().dev("found in piston_heads tag: " + block);
            found = state.is(Tags.PISTON_HEADS);
        }

        if (found) return true;

        // Fallthrough to default behavior
        return state.is(block);
    }
}
