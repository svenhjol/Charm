package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Adds a recipe for Gilded Blackstone.")
public class CraftableGildedBlackstone extends CharmModule {
    public static CraftableGildedBlackstone GILDED_BLACKSTONE;

    @Override
    public void register() {
        GILDED_BLACKSTONE = new CraftableGildedBlackstone();
    }
}
