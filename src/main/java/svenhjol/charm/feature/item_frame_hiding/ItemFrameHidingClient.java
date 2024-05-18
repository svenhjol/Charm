package svenhjol.charm.feature.item_frame_hiding;

import svenhjol.charm.feature.item_frame_hiding.client.Handlers;
import svenhjol.charm.feature.item_frame_hiding.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class ItemFrameHidingClient extends ClientFeature implements CommonResolver<ItemFrameHiding> {
    public final Registers registers;
    public final Handlers handlers;

    public ItemFrameHidingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<ItemFrameHiding> typeForCommon() {
        return ItemFrameHiding.class;
    }
}
