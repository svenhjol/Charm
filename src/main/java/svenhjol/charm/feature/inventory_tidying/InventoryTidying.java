package svenhjol.charm.feature.inventory_tidying;

import svenhjol.charm.feature.inventory_tidying.common.Advancements;
import svenhjol.charm.feature.inventory_tidying.common.Handlers;
import svenhjol.charm.feature.inventory_tidying.common.Networking;
import svenhjol.charm.feature.inventory_tidying.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Button to automatically tidy inventories.")
public final class InventoryTidying extends CommonFeature {
    public final Registers registers;
    public final Networking networking;
    public final Handlers handlers;
    public final Advancements advancements;

    public InventoryTidying(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        networking = new Networking(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
