package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

public class BrewBottleBlock extends MesonBlock {
    public BrewBottleBlock(MesonModule module) {
        super(module, "brew_bottle", AbstractBlock.Settings.copy(Blocks.GLASS));
    }
}
