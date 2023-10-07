package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false)
public class CopperPistons extends CharmonyFeature {
    public static Supplier<Block> copperPistonBlock;
    public static Supplier<Block> copperPistonHeadBlock;
    public static Supplier<Block> movingCopperPistonBlock;
    public static Supplier<Block> stickyCopperPistonBlock;
    public static Supplier<Item> copperPistonBlockItem;
    public static Supplier<Item> stickyCopperPistonBlockItem;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        copperPistonBlock = registry.block("copper_piston", CopperPistonBaseBlock::new);
        copperPistonHeadBlock = registry.block("copper_piston_head", CopperPistonHeadBlock::new);
        movingCopperPistonBlock = registry.block("moving_copper_piston", MovingCopperPiston::new);
        stickyCopperPistonBlock = registry.block("sticky_copper_piston", StickyCopperPistonBaseBlock::new);

        copperPistonBlockItem = registry.item("copper_piston",
            () -> new CopperPistonBaseBlock.BlockItem(this, copperPistonBlock));
        stickyCopperPistonBlockItem = registry.item("sticky_copper_piston",
            () -> new StickyCopperPistonBaseBlock.BlockItem(this, stickyCopperPistonBlock));

        registry.blockEntityBlocks(() -> BlockEntityType.PISTON, List.of(movingCopperPistonBlock));
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
        var log = Charm.instance().log();
        log.debug(CopperPistons.class, message);
    }
}
