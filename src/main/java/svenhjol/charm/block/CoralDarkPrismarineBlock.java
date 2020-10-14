package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ICoralMaterial;

public class CoralDarkPrismarineBlock extends MesonBlock {
    public CoralDarkPrismarineBlock(MesonModule module, ICoralMaterial type) {
        super(module, type.asString() + "_dark_prismarine", Settings.copy(Blocks.DARK_PRISMARINE));
    }
}
