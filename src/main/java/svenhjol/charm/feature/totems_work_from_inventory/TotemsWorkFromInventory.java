package svenhjol.charm.feature.totems_work_from_inventory;

import svenhjol.charm.feature.totems_work_from_inventory.common.Advancements;
import svenhjol.charm.feature.totems_work_from_inventory.common.Handlers;
import svenhjol.charm.feature.totems_work_from_inventory.common.Providers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    A totem will work from anywhere in the player's inventory as well as held in the main or offhand.
    This includes the Totem of Preserving, if enabled.""")
public final class TotemsWorkFromInventory extends CommonFeature {
    public final Providers providers;
    public final Handlers handlers;
    public final Advancements advancements;

    public TotemsWorkFromInventory(CommonLoader loader) {
        super(loader);

        providers = new Providers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
