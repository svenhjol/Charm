package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.feature.clear_item_frames.client.Handlers;
import svenhjol.charm.feature.clear_item_frames.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class ClearItemFramesClient extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final ClearItemFrames common;

    public ClearItemFramesClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ClearItemFrames.class);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
