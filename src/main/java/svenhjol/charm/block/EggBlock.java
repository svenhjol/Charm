package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.module.core.StorageBlocks;

public class EggBlock extends CharmBlock {
    public EggBlock(CharmModule module) {
        super(module, "egg_block", AbstractBlock.Settings.copy(Blocks.SOUL_SAND));
    }

    @Override
    public boolean enabled() {
        return module.enabled && StorageBlocks.egg;
    }
}
