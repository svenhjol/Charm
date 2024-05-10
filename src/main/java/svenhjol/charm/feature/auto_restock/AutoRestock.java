package svenhjol.charm.feature.auto_restock;

import svenhjol.charm.feature.auto_restock.common.Advancements;
import svenhjol.charm.feature.auto_restock.common.Handlers;
import svenhjol.charm.feature.auto_restock.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.enums.Side;

@Feature(description = "Refills hotbar from your inventory.")
public class AutoRestock extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    public AutoRestock(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
