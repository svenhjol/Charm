package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;

public class GoldLanternBlock extends BaseLanternBlock {
    public GoldLanternBlock(MesonModule module, String name) {
        super(module, name, AbstractBlock.Settings.copy(Blocks.LANTERN));
    }
}
