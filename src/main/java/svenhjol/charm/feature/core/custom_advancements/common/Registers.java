package svenhjol.charm.feature.core.custom_advancements.common;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CustomAdvancements> {
    public final ActionPerformed actionPerformed;

    public Registers(CustomAdvancements feature) {
        super(feature);

        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());
    }
}
