package svenhjol.charm.feature.extractable_enchantments;

import svenhjol.charm.feature.extractable_enchantments.client.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class ExtractableEnchantmentsClient extends ClientFeature implements CommonResolver<ExtractableEnchantments> {
    public final Handlers handlers;

    public ExtractableEnchantmentsClient(ClientLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public Class<ExtractableEnchantments> typeForCommon() {
        return ExtractableEnchantments.class;
    }
}
