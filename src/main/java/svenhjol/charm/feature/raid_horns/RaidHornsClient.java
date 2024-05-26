package svenhjol.charm.feature.raid_horns;

import svenhjol.charm.feature.raid_horns.client.Handlers;
import svenhjol.charm.feature.raid_horns.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public class RaidHornsClient extends ClientFeature implements CommonResolver<RaidHorns> {
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
