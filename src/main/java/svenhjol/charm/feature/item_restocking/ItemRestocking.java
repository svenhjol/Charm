package svenhjol.charm.feature.item_restocking;

import svenhjol.charm.feature.item_restocking.common.Advancements;
import svenhjol.charm.feature.item_restocking.common.Handlers;
import svenhjol.charm.feature.item_restocking.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Refills hotbar from your inventory.")
public final class ItemRestocking extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    public ItemRestocking(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
