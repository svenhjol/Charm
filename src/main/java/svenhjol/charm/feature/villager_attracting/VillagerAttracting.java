package svenhjol.charm.feature.villager_attracting;

import svenhjol.charm.feature.villager_attracting.common.Advancements;
import svenhjol.charm.feature.villager_attracting.common.Handlers;
import svenhjol.charm.feature.villager_attracting.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Attract villagers by holding a desired item. By default this is an emerald block.
    The item tag 'villager_loved' can be used to configure the items that attract a villager.""")
public class VillagerAttracting extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public VillagerAttracting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
