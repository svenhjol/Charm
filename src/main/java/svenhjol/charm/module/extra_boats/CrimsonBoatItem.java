package svenhjol.charm.module.extra_boats;

import svenhjol.charm.module.CharmModule;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.module.extra_boats.CharmBoatEntity;

public class CrimsonBoatItem extends CharmBoatItem {
    public CrimsonBoatItem(CharmModule module) {
        super(module, "crimson_boat", CharmBoatEntity.BoatType.CRIMSON, new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(CreativeModeTab.TAB_TRANSPORTATION));
    }
}
