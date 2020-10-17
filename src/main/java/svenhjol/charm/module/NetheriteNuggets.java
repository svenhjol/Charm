package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.item.NetheriteNuggetItem;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Netherite nuggets can be combined to create a netherite ingot.")
public class NetheriteNuggets extends CharmModule {
    public static NetheriteNuggetItem NETHERITE_NUGGET;

    @Override
    public void register() {
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
