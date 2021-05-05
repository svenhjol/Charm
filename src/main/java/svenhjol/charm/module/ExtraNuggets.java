package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.item.CopperNuggetItem;
import svenhjol.charm.item.NetheriteNuggetItem;

@Module(mod = Charm.MOD_ID, description = "Adds copper and netherite nuggets for lantern and chain recipes.")
public class ExtraNuggets extends CharmModule {
    public static CopperNuggetItem COPPER_NUGGET;
    public static NetheriteNuggetItem NETHERITE_NUGGET;

    @Override
    public void register() {
        COPPER_NUGGET = new CopperNuggetItem(this);
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
