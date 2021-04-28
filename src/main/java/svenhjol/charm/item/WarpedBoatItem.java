package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.entity.CharmBoatEntity;

public class WarpedBoatItem extends CharmBoatItem {
    public WarpedBoatItem(CharmModule module) {
        super(module, "warped_boat", CharmBoatEntity.BoatType.WARPED, new Item.Settings()
            .maxCount(1)
            .fireproof()
            .group(ItemGroup.TRANSPORTATION));
    }
}
