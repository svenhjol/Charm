package svenhjol.charm.feature.tooltip_improvements.client;

import svenhjol.charm.api.event.TooltipComponentEvent;
import svenhjol.charm.api.event.TooltipItemHoverEvent;
import svenhjol.charm.api.event.TooltipRenderEvent;
import svenhjol.charm.feature.tooltip_improvements.TooltipImprovements;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<TooltipImprovements> {
    public Registers(TooltipImprovements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        if (feature().showMaps()) {
            TooltipRenderEvent.INSTANCE.handle(feature().handlers::hoverOverMap);
        }

        if (feature().showShulkerBoxes()) {
            TooltipItemHoverEvent.INSTANCE.handle(feature().handlers::removeLinesFromShulkerBox);
            TooltipComponentEvent.INSTANCE.handle(feature().handlers::addGridToShulkerBox);
        }
    }
}
