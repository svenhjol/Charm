package svenhjol.charm.feature.raid_horns;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.raid_horns.client.Handlers;
import svenhjol.charm.feature.raid_horns.client.Registers;

@Feature
public final class RaidHornsClient extends ClientFeature implements LinkedFeature<RaidHorns> {
    public final Registers registers;
    public final Handlers handlers;

    public RaidHornsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<RaidHorns> typeForLinked() {
        return RaidHorns.class;
    }
}
