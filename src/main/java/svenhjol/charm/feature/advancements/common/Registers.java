package svenhjol.charm.feature.advancements.common;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<Advancements> {
    public final ActionPerformed actionPerformed;
    public final List<IConditionalAdvancement> conditions = new ArrayList<>();

    public Registers(Advancements feature) {
        super(feature);
        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> conditions.addAll(provider.getAdvancementConditions()));
    }
}
