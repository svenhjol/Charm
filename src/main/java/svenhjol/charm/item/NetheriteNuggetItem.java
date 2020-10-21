package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmItem;

public class NetheriteNuggetItem extends CharmItem {
    public NetheriteNuggetItem(CharmModule module) {
        super(module, "netherite_nugget", new Item.Settings()
            .group(ItemGroup.MATERIALS)
            .fireproof());
    }
}
