package svenhjol.charm.feature.player_pressure_plates;

import svenhjol.charm.feature.player_pressure_plates.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class PlayerPressurePlatesClient extends ClientFeature implements CommonResolver<PlayerPressurePlates> {
    public final Registers registers;

    public PlayerPressurePlatesClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<PlayerPressurePlates> typeForCommon() {
        return PlayerPressurePlates.class;
    }
}
