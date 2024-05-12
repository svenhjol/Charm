package svenhjol.charm.feature.extra_tooltips.client;

import svenhjol.charm.api.event.TooltipComponentEvent;
import svenhjol.charm.api.event.TooltipItemHoverEvent;
import svenhjol.charm.api.event.TooltipRenderEvent;
import svenhjol.charm.feature.extra_tooltips.ExtraTooltips;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ExtraTooltips> {
    public Registers(ExtraTooltips feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        if (ExtraTooltips.showMaps) {
            TooltipRenderEvent.INSTANCE.handle(feature().handlers::hoverOverMap);
        }

        if (ExtraTooltips.showShulkerBoxes) {
            TooltipItemHoverEvent.INSTANCE.handle(feature().handlers::removeLinesFromShulkerBox);
            TooltipComponentEvent.INSTANCE.handle(feature().handlers::addGridToShulkerBox);
        }
    }
}
