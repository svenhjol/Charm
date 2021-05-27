package svenhjol.charm.module.coral_sea_lanterns;

import net.minecraft.block.Blocks;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.enums.ICoralMaterial;

public class CoralSeaLanternBlock extends CharmBlock {
    public CoralSeaLanternBlock(CharmModule module, ICoralMaterial type) {
        super(module, type.asString() + "_sea_lantern", Settings.copy(Blocks.SEA_LANTERN));
    }
}
