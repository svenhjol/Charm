package svenhjol.charm.feature.player_pressure_plates;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.player_pressure_plates.common.Providers;
import svenhjol.charm.feature.player_pressure_plates.common.Registers;

@Feature(description = "Player-only pressure plates crafted using smooth basalt.")
public final class PlayerPressurePlates extends CommonFeature {
    public final Registers registers;
    public final Providers providers;

    public PlayerPressurePlates(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }
}
