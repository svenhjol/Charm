package svenhjol.charm.decoration.module;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.GoldLanternBlock;
import svenhjol.charm.decoration.client.GoldLanternsClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Gold version of the vanilla lantern.")
public class GoldLanterns extends MesonModule {
    public static GoldLanternBlock block;

    public static GoldLanternsClient client;

    @Override
    public void init() {
        block = new GoldLanternBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new GoldLanternsClient();
    }
}
