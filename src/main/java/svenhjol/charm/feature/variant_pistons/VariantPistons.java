package svenhjol.charm.feature.variant_pistons;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false)
public class VariantPistons extends CharmonyFeature {
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
        var log = Charm.instance().log();
        log.debug(VariantPistons.class, message);
    }
}
