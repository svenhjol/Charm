package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.module.storageblocks.StorageBlocks;

public class RottenFleshBlock extends CharmBlock {
    public RottenFleshBlock(CharmModule module) {
        super(module, "rotten_flesh_block", AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK));
    }

    @Override
    public boolean enabled() {
        return module.enabled && StorageBlocks.rottenflesh;
    }
}
