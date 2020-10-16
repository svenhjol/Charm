package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ICoralMaterial;

public class CoralSeaLanternBlock extends MesonBlock {
    public CoralSeaLanternBlock(MesonModule module, ICoralMaterial type) {
        super(module, type.asString() + "_sea_lantern", Settings.copy(Blocks.SEA_LANTERN));
    }
}
