package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CopperRailBlock;
import svenhjol.charm.client.CopperRailsClient;

@Module(mod = Charm.MOD_ID, client = CopperRailsClient.class, description = "Copper ingots can be used to craft rails.")
public class CopperRails extends CharmModule {
    public static CopperRailBlock COPPER_RAIL;

    @Override
    public void register() {
        COPPER_RAIL = new CopperRailBlock(this);
    }
}
