package svenhjol.charm.feature.tooltip_improvements.client;

import svenhjol.charm.charmony.event.TooltipComponentEvent;
import svenhjol.charm.charmony.event.TooltipItemHoverEvent;
import svenhjol.charm.charmony.event.TooltipRenderEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.tooltip_improvements.TooltipImprovements;

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
