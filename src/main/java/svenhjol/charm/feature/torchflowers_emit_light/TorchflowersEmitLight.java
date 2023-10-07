package svenhjol.charm.feature.torchflowers_emit_light;

import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.base.CharmonyFeature;

@Feature(
    mod = CharmClient.MOD_ID,
    description = "Torchflowers emit ambient light."
)
public class TorchflowersEmitLight extends CharmonyFeature {
    @Configurable(
        name = "Light level",
        description = "Amount of light emitted by a Torchflower. Valid values between 0-15."
    )
    public static int lightLevel = 8;
}
