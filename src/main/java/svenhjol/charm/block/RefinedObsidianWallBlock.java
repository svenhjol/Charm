package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.WallBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.module.RefinedObsidian;

public class RefinedObsidianWallBlock extends WallBlock implements ICharmBlock {
    private CharmModule module;

    public RefinedObsidianWallBlock(CharmModule module) {
        super(AbstractBlock.Settings.copy(RefinedObsidian.REFINED_OBSIDIAN));
        this.register(module, "refined_obsidian_wall");
        this.module = module;
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
