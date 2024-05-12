package svenhjol.charm.feature.collection;

import svenhjol.charm.feature.collection.common.Advancements;
import svenhjol.charm.feature.collection.common.Handlers;
import svenhjol.charm.feature.collection.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Tools with the Collection enchantment automatically pick up drops.")
public class Collection extends CommonFeature {
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