package svenhjol.charm.module.extra_boats;

import net.minecraft.world.item.CreativeModeTab;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.loader.CharmModule;

public class CrimsonBoatItem extends CharmBoatItem {
    public CrimsonBoatItem(CharmModule module) {
        super(module, "crimson_boat", ExtraBoats.CRIMSON, new Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(CreativeModeTab.TAB_TRANSPORTATION));

        this.setFireproof();
    }
}
