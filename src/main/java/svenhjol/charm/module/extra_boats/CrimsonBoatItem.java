package svenhjol.charm.module.extra_boats;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmBoatItem;

public class CrimsonBoatItem extends CharmBoatItem {
    public CrimsonBoatItem(CharmModule module) {
        super(module, "crimson_boat", CharmBoatEntity.BoatType.CRIMSON, new Item.Settings()
            .maxCount(1)
            .fireproof()
            .group(ItemGroup.TRANSPORTATION));
    }
}
