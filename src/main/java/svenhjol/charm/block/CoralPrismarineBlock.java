package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ICoralMaterial;

public class CoralPrismarineBlock extends MesonBlock {
    public CoralPrismarineBlock(MesonModule module, ICoralMaterial type) {
        super(module, type.asString() + "_prismarine", Settings.copy(Blocks.PRISMARINE));
    }
}
