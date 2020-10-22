package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;

public class RefinedObsidianBlock extends CharmBlock {
    public RefinedObsidianBlock(CharmModule module) {
        super(module, "refined_obsidian", AbstractBlock.Settings
            .copy(Blocks.OBSIDIAN)
            .sounds(BlockSoundGroup.BASALT)
        );
    }
}
