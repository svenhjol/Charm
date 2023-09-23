package svenhjol.charm.feature.piston_test;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false)
public class PistonTest extends CharmFeature {
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

        if (defaultState.is(Blocks.MOVING_PISTON)) {
            debug("found in moving_pistons tag: " + block);
            return state.is(CharmTags.MOVING_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON)) {
            debug("found in pistons tag: " + block);
            return state.is(CharmTags.PISTONS);
        }
        if (defaultState.is(Blocks.STICKY_PISTON)) {
            debug("found in sticky_pistons tag: " + block);
            return state.is(CharmTags.STICKY_PISTONS);
        }
        if (defaultState.is(Blocks.MOVING_PISTON)) {
            debug("found in moving_pistons tag: " + block);
            return state.is(CharmTags.MOVING_PISTONS);
        }
        if (defaultState.is(Blocks.PISTON_HEAD)) {
            debug("found in piston_heads tag: " + block);
            return state.is(CharmTags.PISTON_HEADS);
        }

        // Fallthrough to default behavior
        return state.is(block);
    }

    public static Optional<BlockState> tryMapState(BlockState defaultState) {
        if (defaultState.is(Blocks.PISTON_HEAD)) {
            debug("remapping piston_head to copper_piston_head");
            return Optional.of(PistonTest.copperPistonHeadBlock.get().withPropertiesOf(defaultState));
        }
        if (defaultState.is(Blocks.MOVING_PISTON)) {
            debug("remapping moving_piston to moving_copper_piston");
            return Optional.of(PistonTest.movingCopperPistonBlock.get().withPropertiesOf(defaultState));
        }
        return Optional.empty();
    }

    public static void debug(String message) {
        var log = Charm.instance().log();
        log.debug(PistonTest.class, message);
    }
}
