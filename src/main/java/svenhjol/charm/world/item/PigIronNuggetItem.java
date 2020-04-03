package svenhjol.charm.world.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.MesonItem;
import svenhjol.meson.MesonModule;

public class PigIronNuggetItem extends MesonItem {
    public PigIronNuggetItem(MesonModule module) {
        super(module, "pig_iron_nugget", new Item.Properties()
            .group(ItemGroup.MATERIALS));
    }
}
