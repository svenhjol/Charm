package svenhjol.charm.feature.extractable_enchantments;

import svenhjol.charm.feature.extractable_enchantments.client.Handlers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public class ExtractableEnchantmentsClient extends ClientFeature {
    public final ExtractableEnchantments common;
    public final Handlers handlers;

    public ExtractableEnchantmentsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ExtractableEnchantments.class);
        handlers = new Handlers(this);
    }
}
