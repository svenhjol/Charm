package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.StairsBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.module.RefinedObsidian;

public class RefinedObsidianStairsBlock extends StairsBlock implements ICharmBlock {
    private CharmModule module;

    public RefinedObsidianStairsBlock(CharmModule module) {
        super(RefinedObsidian.REFINED_OBSIDIAN.getDefaultState(), AbstractBlock.Settings.copy(RefinedObsidian.REFINED_OBSIDIAN));
        this.register(module, "refined_obsidian_stairs");
        this.module = module;
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
