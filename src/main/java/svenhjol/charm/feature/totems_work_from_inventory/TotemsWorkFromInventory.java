package svenhjol.charm.feature.totems_work_from_inventory;

import svenhjol.charm.feature.totems_work_from_inventory.common.Advancements;
import svenhjol.charm.feature.totems_work_from_inventory.common.Handlers;
import svenhjol.charm.feature.totems_work_from_inventory.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    A totem will work from anywhere in the player's inventory as well as held in the main or offhand.
    This includes the Totem of Preserving, if enabled.""")
public class TotemsWorkFromInventory extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public TotemsWorkFromInventory(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
