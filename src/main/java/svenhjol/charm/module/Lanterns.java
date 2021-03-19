package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CopperLanternBlock;
import svenhjol.charm.block.GoldLanternBlock;
import svenhjol.charm.client.LanternsClient;

@Module(mod = Charm.MOD_ID, client = LanternsClient.class, description = "Gold version of the vanilla lanterns.")
public class Lanterns extends CharmModule {
    public static GoldLanternBlock GOLD_LANTERN;
    public static GoldLanternBlock GOLD_SOUL_LANTERN;
    public static CopperLanternBlock COPPER_LANTERN;
    public static CopperLanternBlock COPPER_SOUL_LANTERN;


    @Override
    public void register() {
        GOLD_LANTERN = new GoldLanternBlock(this, "gold_lantern");
        GOLD_SOUL_LANTERN = new GoldLanternBlock(this, "gold_soul_lantern");
        COPPER_LANTERN = new CopperLanternBlock(this, "copper_lantern");
        COPPER_SOUL_LANTERN = new CopperLanternBlock(this, "copper_soul_lantern");
    }
}
