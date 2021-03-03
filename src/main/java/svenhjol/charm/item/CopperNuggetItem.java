package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmItem;

public class CopperNuggetItem extends CharmItem {
    public CopperNuggetItem(CharmModule module) {
        super(module, "copper_nugget", new Item.Settings()
            .group(ItemGroup.MATERIALS));
    }
}
