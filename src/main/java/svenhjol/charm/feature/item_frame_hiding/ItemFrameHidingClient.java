package svenhjol.charm.feature.item_frame_hiding;

import svenhjol.charm.feature.item_frame_hiding.client.Handlers;
import svenhjol.charm.feature.item_frame_hiding.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

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
