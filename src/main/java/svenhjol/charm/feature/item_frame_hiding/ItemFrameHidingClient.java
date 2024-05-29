package svenhjol.charm.feature.item_frame_hiding;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.item_frame_hiding.client.Handlers;
import svenhjol.charm.feature.item_frame_hiding.client.Registers;

@Feature
public final class ItemFrameHidingClient extends ClientFeature implements LinkedFeature<ItemFrameHiding> {
    public final Registers registers;
    public final Handlers handlers;

    public ItemFrameHidingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<ItemFrameHiding> typeForLinked() {
        return ItemFrameHiding.class;
    }
}
