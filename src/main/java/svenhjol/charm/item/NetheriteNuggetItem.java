package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.MesonModule;
import svenhjol.meson.item.MesonItem;

public class NetheriteNuggetItem extends MesonItem {
    public NetheriteNuggetItem(MesonModule module) {
        super(module, "netherite_nugget", new Item.Settings()
            .group(ItemGroup.MATERIALS)
            .fireproof());
    }
}
