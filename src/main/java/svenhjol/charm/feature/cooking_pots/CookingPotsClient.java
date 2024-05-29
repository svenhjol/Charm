package svenhjol.charm.feature.cooking_pots;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.cooking_pots.client.Handlers;
import svenhjol.charm.feature.cooking_pots.client.Registers;

@Feature
public final class CookingPotsClient extends ClientFeature implements LinkedFeature<CookingPots> {
    public final Registers registers;
    public final Handlers handlers;

    public CookingPotsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<CookingPots> typeForLinked() {
        return CookingPots.class;
    }
}
