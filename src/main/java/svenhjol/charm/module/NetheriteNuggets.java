package svenhjol.charm.module;

import svenhjol.charm.item.NetheriteNuggetItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Netherite nuggets can be combined to create a netherite ingot.")
public class NetheriteNuggets extends MesonModule {
    public static NetheriteNuggetItem NETHERITE_NUGGET;

    @Override
    public void register() {
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
