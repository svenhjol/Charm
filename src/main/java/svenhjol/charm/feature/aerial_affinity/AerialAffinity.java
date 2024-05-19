package svenhjol.charm.feature.aerial_affinity;

import svenhjol.charm.feature.aerial_affinity.common.Advancements;
import svenhjol.charm.feature.aerial_affinity.common.Handlers;
import svenhjol.charm.feature.aerial_affinity.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinity extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public AerialAffinity(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}