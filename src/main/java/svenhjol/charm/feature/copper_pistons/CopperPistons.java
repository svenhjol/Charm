package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Copper Pistons do not have quasi-connectivity.")
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
        movingCopperPistonBlock = registry.block("moving_copper_piston", MovingCopperPistonBlock::new);
        stickyCopperPistonBlock = registry.block("sticky_copper_piston", StickyCopperPistonBaseBlock::new);

        copperPistonBlockItem = registry.item("copper_piston",
            () -> new CopperPistonBaseBlock.BlockItem(this, copperPistonBlock));
        stickyCopperPistonBlockItem = registry.item("sticky_copper_piston",
            () -> new StickyCopperPistonBaseBlock.BlockItem(this, stickyCopperPistonBlock));

        registry.blockEntityBlocks(() -> BlockEntityType.PISTON, List.of(movingCopperPistonBlock));
    }
}
