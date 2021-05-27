package svenhjol.charm.module.extra_boats;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmBoatItem;

public class WarpedBoatItem extends CharmBoatItem {
    public WarpedBoatItem(CharmModule module) {
        super(module, "warped_boat", CharmBoatEntity.BoatType.WARPED, new Item.Settings()
            .maxCount(1)
            .fireproof()
            .group(ItemGroup.TRANSPORTATION));
    }
}
