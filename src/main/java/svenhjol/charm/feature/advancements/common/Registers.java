package svenhjol.charm.feature.advancements.common;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends Register<Advancements> {
    public ActionPerformed actionPerformed;
    public List<IConditionalAdvancement> conditions = new ArrayList<>();

    public Registers() {
        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> conditions.addAll(provider.getAdvancementConditions()));
    }

    @Override
    protected Class<Advancements> type() {
        return Advancements.class;
    }
}
