package svenhjol.charm.feature.torchflowers_emit_light;

import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.base.CharmFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Torchflowers emit ambient light. This light does not affect hostile mob spawning."
)
public class TorchflowersEmitLight extends CharmFeature {
    @Configurable(
        name = "Light level",
        description = "Amount of light emitted by a Torchflower. Valid values between 0-15."
    )
    public static int lightLevel = 8;
}
