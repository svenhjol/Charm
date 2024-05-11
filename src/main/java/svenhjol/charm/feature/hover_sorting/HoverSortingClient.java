package svenhjol.charm.feature.hover_sorting;

import svenhjol.charm.feature.hover_sorting.client.Handlers;
import svenhjol.charm.feature.hover_sorting.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class HoverSortingClient extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;

    public HoverSortingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
