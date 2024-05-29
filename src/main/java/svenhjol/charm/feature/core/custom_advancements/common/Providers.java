package svenhjol.charm.feature.core.custom_advancements.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.charmony.Api;

import java.util.ArrayList;
import java.util.List;

public final class Providers extends ProviderHolder<CustomAdvancements> {
    public final List<ConditionalAdvancement> conditions = new ArrayList<>();

    public Providers(CustomAdvancements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        Api.consume(ConditionalAdvancementProvider.class,
            provider -> conditions.addAll(provider.getAdvancementConditions()));
    }
}
