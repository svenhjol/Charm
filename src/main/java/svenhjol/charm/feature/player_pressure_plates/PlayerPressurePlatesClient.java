package svenhjol.charm.feature.player_pressure_plates;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.player_pressure_plates.client.Registers;

@Feature
public final class PlayerPressurePlatesClient extends ClientFeature implements LinkedFeature<PlayerPressurePlates> {
    public final Registers registers;

    public PlayerPressurePlatesClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<PlayerPressurePlates> typeForLinked() {
        return PlayerPressurePlates.class;
    }
}
