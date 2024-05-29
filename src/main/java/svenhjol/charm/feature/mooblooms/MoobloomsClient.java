package svenhjol.charm.feature.mooblooms;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.mooblooms.client.Registers;

@Feature
public final class MoobloomsClient extends ClientFeature implements LinkedFeature<Mooblooms> {
    public final Registers registers;

    public MoobloomsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Mooblooms> typeForLinked() {
        return Mooblooms.class;
    }
}
