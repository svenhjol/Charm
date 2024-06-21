package svenhjol.charm.feature.item_tidying;

import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.item_tidying.client.Handlers;
import svenhjol.charm.feature.item_tidying.client.Providers;
import svenhjol.charm.feature.item_tidying.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;

@Feature
public final class ItemTidyingClient extends ClientFeature implements LinkedFeature<ItemTidying> {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public ItemTidyingClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<ItemTidying> typeForLinked() {
        return ItemTidying.class;
    }
}
