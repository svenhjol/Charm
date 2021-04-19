package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.item.CopperNuggetItem;

@Module(mod = Charm.MOD_ID, description = "Copper nuggets can be combined to create a copper ingot.")
public class CopperNuggets extends CharmModule {
    public static CopperNuggetItem COPPER_NUGGET;

    @Override
    public void register() {
        COPPER_NUGGET = new CopperNuggetItem(this);
    }
}
