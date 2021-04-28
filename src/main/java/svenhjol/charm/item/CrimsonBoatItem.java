package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.entity.CharmBoatEntity;

public class CrimsonBoatItem extends CharmBoatItem {
    public CrimsonBoatItem(CharmModule module) {
        super(module, "crimson_boat", CharmBoatEntity.BoatType.CRIMSON, new Item.Settings()
            .maxCount(1)
            .fireproof()
            .group(ItemGroup.TRANSPORTATION));
    }
}
