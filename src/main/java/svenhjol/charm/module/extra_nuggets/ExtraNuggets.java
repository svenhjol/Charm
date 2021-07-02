package svenhjol.charm.module.extra_nuggets;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, priority = 1, description = "Adds copper and netherite nuggets for lantern and chain recipes.")
public class ExtraNuggets extends CharmModule {
    public static CopperNuggetItem COPPER_NUGGET;
    public static NetheriteNuggetItem NETHERITE_NUGGET;

    @Override
    public void register() {
        COPPER_NUGGET = new CopperNuggetItem(this);
        NETHERITE_NUGGET = new NetheriteNuggetItem(this);
    }
}
