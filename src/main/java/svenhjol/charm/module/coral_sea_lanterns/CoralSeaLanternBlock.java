package svenhjol.charm.module.coral_sea_lanterns;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.enums.ICoralMaterial;

public class CoralSeaLanternBlock extends CharmBlock {
    public CoralSeaLanternBlock(CommonModule module, ICoralMaterial type) {
        super(module, type.getSerializedName() + "_sea_lantern", Properties.copy(Blocks.SEA_LANTERN));
    }
}
