package svenhjol.charm.feature.collection;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.collection.common.Advancements;
import svenhjol.charm.feature.collection.common.Handlers;
import svenhjol.charm.feature.collection.common.Registers;

@Feature(description = "Tools with the Collection enchantment automatically pick up drops.")
public final class Collection extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    public Collection(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}