package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.feature.clear_item_frames.client.Handlers;
import svenhjol.charm.feature.clear_item_frames.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class ClearItemFramesClient extends ClientFeature implements CommonResolver<ClearItemFrames> {
    public final Registers registers;
    public final Handlers handlers;

    public ClearItemFramesClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<ClearItemFrames> typeForCommon() {
        return ClearItemFrames.class;
    }
}
