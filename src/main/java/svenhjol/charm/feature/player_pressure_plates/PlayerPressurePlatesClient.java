package svenhjol.charm.feature.player_pressure_plates;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.player_pressure_plates.client.Registers;

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
