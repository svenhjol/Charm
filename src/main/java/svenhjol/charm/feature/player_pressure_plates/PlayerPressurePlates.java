package svenhjol.charm.feature.player_pressure_plates;

import svenhjol.charm.feature.player_pressure_plates.common.Providers;
import svenhjol.charm.feature.player_pressure_plates.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Player-only pressure plates crafted using smooth basalt.")
public class PlayerPressurePlates extends CommonFeature {
    public final Registers registers;
    public final Providers providers;

    public PlayerPressurePlates(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }
}
