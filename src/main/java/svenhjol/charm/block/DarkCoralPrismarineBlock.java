package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ICoralMaterial;

public class DarkCoralPrismarineBlock extends MesonBlock {
    public DarkCoralPrismarineBlock(MesonModule module, ICoralMaterial type) {
        super(module, "dark_" + type.asString() + "_prismarine", Settings.copy(Blocks.DARK_PRISMARINE));
    }
}
