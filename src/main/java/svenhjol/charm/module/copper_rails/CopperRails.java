package svenhjol.charm.module.copper_rails;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, client = CopperRailsClient.class, description = "Copper ingots can be used to craft rails.")
public class CopperRails extends CharmModule {
    public static CopperRailBlock COPPER_RAIL;

    @Override
    public void register() {
        COPPER_RAIL = new CopperRailBlock(this);
    }
}
