package svenhjol.charm.feature.cooking_pots;

import svenhjol.charm.feature.cooking_pots.client.Handlers;
import svenhjol.charm.feature.cooking_pots.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CookingPotsClient extends ClientFeature implements CommonResolver<CookingPots> {
    public final Registers registers;
    public final Handlers handlers;

    public CookingPotsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<CookingPots> typeForCommon() {
        return CookingPots.class;
    }
}
