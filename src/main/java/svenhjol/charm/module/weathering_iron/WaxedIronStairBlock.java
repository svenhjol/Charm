package svenhjol.charm.module.weathering_iron;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.block.CharmStairBlock;
import svenhjol.charm.loader.CharmModule;

public class WaxedIronStairBlock extends CharmStairBlock implements IWaxableIron {
    public WaxedIronStairBlock(CharmModule module, String name, Block block) {
        super(module, name, block);
    }
}
