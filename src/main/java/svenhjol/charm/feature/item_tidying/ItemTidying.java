package svenhjol.charm.feature.item_tidying;

import svenhjol.charm.feature.item_tidying.common.Advancements;
import svenhjol.charm.feature.item_tidying.common.Handlers;
import svenhjol.charm.feature.item_tidying.common.Networking;
import svenhjol.charm.feature.item_tidying.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Button to automatically tidy items in inventories.")
public final class ItemTidying extends CommonFeature {
    public final Registers registers;
    public final Networking networking;
    public final Handlers handlers;
    public final Advancements advancements;

    public ItemTidying(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        networking = new Networking(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
