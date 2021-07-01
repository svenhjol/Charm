package svenhjol.charm.module.extra_nuggets;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.world.item.CreativeModeTab;
import svenhjol.charm.item.CharmItem;

public class NetheriteNuggetItem extends CharmItem {
    public NetheriteNuggetItem(CommonModule module) {
        super(module, "netherite_nugget", new Properties()
            .tab(CreativeModeTab.TAB_MATERIALS));
    }
}
