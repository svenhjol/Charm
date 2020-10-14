package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ICoralMaterial;

public class CoralPrismarineBricksBlock extends MesonBlock {
    public CoralPrismarineBricksBlock(MesonModule module, ICoralMaterial type) {
        super(module, type.asString() + "_prismarine_bricks", Settings.copy(Blocks.PRISMARINE_BRICKS));
    }
}
