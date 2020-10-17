package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmItem;

public class BeeswaxItem extends CharmItem {
    public BeeswaxItem(CharmModule module) {
        super(module, "beeswax", new Item.Settings()
            .group(ItemGroup.MATERIALS));

        this.setBurnTime(800);
    }
}
