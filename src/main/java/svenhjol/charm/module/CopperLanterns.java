package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CopperLanternBlock;
import svenhjol.charm.client.CopperLanternsClient;

@Module(mod = Charm.MOD_ID, client = CopperLanternsClient.class, description = "Copper version of the vanilla lanterns.")
public class CopperLanterns extends CharmModule {
    public static CopperLanternBlock COPPER_LANTERN;
    public static CopperLanternBlock COPPER_SOUL_LANTERN;

    @Override
    public void register() {
        COPPER_LANTERN = new CopperLanternBlock(this, "copper_lantern");
        COPPER_SOUL_LANTERN = new CopperLanternBlock(this, "copper_soul_lantern");
    }
}
