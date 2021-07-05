package svenhjol.charm.module.extra_nuggets;

import svenhjol.charm.loader.CharmModule;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import svenhjol.charm.item.CharmItem;

public class CopperNuggetItem extends CharmItem {
    public CopperNuggetItem(CharmModule module) {
        super(module, "copper_nugget", new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS));
    }
}
