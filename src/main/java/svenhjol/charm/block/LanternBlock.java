package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.module.core.StorageBlocks;

public class LanternBlock extends CharmBlock {
    public LanternBlock(CharmModule module) {
        super(module, "lantern_block", AbstractBlock.Settings.copy(Blocks.LANTERN));
    }

    @Override
    public boolean enabled() {
        return module.enabled && StorageBlocks.egg;
    }
}
