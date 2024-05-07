package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.foundation.feature.Register;

import java.util.List;

public final class CommonRegistration extends Register<CopperPistons> {
    public CommonRegistration(CopperPistons feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        CopperPistons.copperPistonBlock = registry.block("copper_piston", CopperPistonBaseBlock::new);
        CopperPistons.copperPistonHeadBlock = registry.block("copper_piston_head", CopperPistonHeadBlock::new);
        CopperPistons.movingCopperPistonBlock = registry.block("moving_copper_piston", MovingCopperPistonBlock::new);
        CopperPistons.stickyCopperPistonBlock = registry.block("sticky_copper_piston", StickyCopperPistonBaseBlock::new);

        CopperPistons.copperPistonBlockItem = registry.item("copper_piston",
            () -> new CopperPistonBaseBlock.BlockItem(CopperPistons.copperPistonBlock));
        CopperPistons.stickyCopperPistonBlockItem = registry.item("sticky_copper_piston",
            () -> new StickyCopperPistonBaseBlock.BlockItem(CopperPistons.stickyCopperPistonBlock));

        registry.blockEntityBlocks(() -> BlockEntityType.PISTON, List.of(CopperPistons.movingCopperPistonBlock));
    }
}
