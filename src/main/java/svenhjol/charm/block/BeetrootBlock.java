package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.module.core.StorageBlocks;

public class BeetrootBlock extends CharmBlock {
    public BeetrootBlock(CharmModule module) {
        super(module, "beetroot_block", AbstractBlock.Settings.copy(Blocks.HAY_BLOCK));
    }

    @Override
    public boolean enabled() {
        return module.enabled && StorageBlocks.beetroot;
    }
}
