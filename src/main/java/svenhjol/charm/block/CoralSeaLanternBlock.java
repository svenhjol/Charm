package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.base.enums.ICoralMaterial;

public class CoralSeaLanternBlock extends CharmBlock {
    public CoralSeaLanternBlock(CharmModule module, ICoralMaterial type) {
        super(module, type.asString() + "_sea_lantern", Settings.copy(Blocks.SEA_LANTERN));
    }
}
