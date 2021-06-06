package svenhjol.charm.module.extra_nuggets;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.extra_nuggets.CopperNuggetItem;
import svenhjol.charm.module.extra_nuggets.NetheriteNuggetItem;

@Module(mod = Charm.MOD_ID, description = "Adds copper and netherite nuggets for lantern and chain recipes.")
public class ExtraNuggets extends CharmModule {
    public static svenhjol.charm.module.extra_nuggets.CopperNuggetItem COPPER_NUGGET;
    public static svenhjol.charm.module.extra_nuggets.NetheriteNuggetItem NETHERITE_NUGGET;

    @Override
    public void register() {
        COPPER_NUGGET = new CopperNuggetItem(this);
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
