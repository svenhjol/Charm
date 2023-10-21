package svenhjol.charm.feature.variant_pistons;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;

public class VariantPistons extends CommonFeature {
    @Override
    public boolean canBeDisabled() {
        return false;
    }

    public static boolean alsoCheckTags(BlockState state, Block block) {
        var defaultState = block.defaultBlockState();
        boolean found = false;

        if (defaultState.is(Blocks.MOVING_PISTON)) {
            debug("found in moving_pistons tag: " + block);
            found = state.is(CharmTags.MOVING_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON)) {
            debug("found in pistons tag: " + block);
            found = state.is(CharmTags.PISTONS);
        }
        if (defaultState.is(Blocks.STICKY_PISTON)) {
            debug("found in sticky_pistons tag: " + block);
            found = state.is(CharmTags.STICKY_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON_HEAD)) {
            debug("found in piston_heads tag: " + block);
            found = state.is(CharmTags.PISTON_HEADS);
        }

        if (found) return true;

        // Fallthrough to default behavior
        return state.is(block);
    }

    public static void debug(String message) {
        var log = Mods.common(Charm.ID).log();
        log.debug(VariantPistons.class, message);
    }
}
