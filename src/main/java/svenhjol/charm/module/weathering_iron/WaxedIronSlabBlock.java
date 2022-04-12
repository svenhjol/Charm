package svenhjol.charm.module.weathering_iron;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.block.CharmSlabBlock;
import svenhjol.charm.loader.CharmModule;

public class WaxedIronSlabBlock extends CharmSlabBlock implements IWaxableIron {
    public WaxedIronSlabBlock(CharmModule module, String name, Block block) {
        super(module, name, Properties.copy(block));
    }
}
