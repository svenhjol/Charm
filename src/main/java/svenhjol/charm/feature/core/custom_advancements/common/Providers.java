package svenhjol.charm.feature.core.custom_advancements.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public final class Providers extends ProviderHolder<CustomAdvancements> {
    public final List<IConditionalAdvancement> conditions = new ArrayList<>();

    public Providers(CustomAdvancements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalAdvancementProvider.class,
            provider -> conditions.addAll(provider.getAdvancementConditions()));
    }
}
