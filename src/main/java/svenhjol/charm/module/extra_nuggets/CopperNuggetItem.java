package svenhjol.charm.module.extra_nuggets;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmItem;

public class CopperNuggetItem extends CharmItem {
    public CopperNuggetItem(CharmModule module) {
        super(module, "copper_nugget", new Item.Settings()
            .group(ItemGroup.MATERIALS));
    }
}
