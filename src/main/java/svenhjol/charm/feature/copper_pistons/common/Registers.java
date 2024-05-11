package svenhjol.charm.feature.copper_pistons.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.feature.copper_pistons.block.CopperPistonBaseBlock;
import svenhjol.charm.feature.copper_pistons.block.CopperPistonHeadBlock;
import svenhjol.charm.feature.copper_pistons.block.MovingCopperPistonBlock;
import svenhjol.charm.feature.copper_pistons.block.StickyCopperPistonBaseBlock;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CopperPistons> {
    public final Supplier<Block> copperPistonBlock;
    public final Supplier<Block> copperPistonHeadBlock;
    public final Supplier<Block> movingCopperPistonBlock;
    public final Supplier<Block> stickyCopperPistonBlock;
    public final Supplier<Item> copperPistonBlockItem;
    public final Supplier<Item> stickyCopperPistonBlockItem;

    public Registers(CopperPistons feature) {
        super(feature);
        var registry = feature.registry();

        copperPistonBlock = registry.block("copper_piston", CopperPistonBaseBlock::new);
        copperPistonHeadBlock = registry.block("copper_piston_head", CopperPistonHeadBlock::new);
        movingCopperPistonBlock = registry.block("moving_copper_piston", MovingCopperPistonBlock::new);
        stickyCopperPistonBlock = registry.block("sticky_copper_piston", StickyCopperPistonBaseBlock::new);

        copperPistonBlockItem = registry.item("copper_piston",
            () -> new CopperPistonBaseBlock.BlockItem(copperPistonBlock));
        stickyCopperPistonBlockItem = registry.item("sticky_copper_piston",
            () -> new StickyCopperPistonBaseBlock.BlockItem(stickyCopperPistonBlock));

        registry.blockEntityBlocks(() -> BlockEntityType.PISTON, List.of(movingCopperPistonBlock));
    }
}
