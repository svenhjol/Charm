package svenhjol.charm.feature.hover_sorting;

import svenhjol.charm.feature.hover_sorting.common.*;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Scroll the mouse while hovering over a bundle or shulker box to cycle the order of its contents.")
public final class HoverSorting extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;
    public final Networking networking;
    public final Providers providers;

    public HoverSorting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
        networking = new Networking(this);
        providers = new Providers(this);
    }
}
