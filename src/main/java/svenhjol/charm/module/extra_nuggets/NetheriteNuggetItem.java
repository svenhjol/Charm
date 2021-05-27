package svenhjol.charm.module.extra_nuggets;

import net.minecraft.item.ItemGroup;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmItem;

public class NetheriteNuggetItem extends CharmItem {
    public NetheriteNuggetItem(CharmModule module) {
        super(module, "netherite_nugget", new Settings()
            .group(ItemGroup.MATERIALS));
    }
}
