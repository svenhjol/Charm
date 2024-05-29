package svenhjol.charm.feature.raid_horns;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.raid_horns.client.Handlers;
import svenhjol.charm.feature.raid_horns.client.Registers;

@Feature
public final class RaidHornsClient extends ClientFeature implements CommonResolver<RaidHorns> {
    public final Registers registers;
    public final Handlers handlers;

    public RaidHornsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<RaidHorns> typeForCommon() {
        return RaidHorns.class;
    }
}
