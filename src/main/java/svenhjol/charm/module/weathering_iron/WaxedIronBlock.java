package svenhjol.charm.module.weathering_iron;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.loader.CharmModule;

public class WaxedIronBlock extends CharmBlock implements IWaxableIron {
    public WaxedIronBlock(CharmModule module, String name, Block block) {
        super(module, name, Properties.copy(block));
    }
}
