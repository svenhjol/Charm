package svenhjol.charm.feature.torchflowers_emit_light;

import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;

public class TorchflowersEmitLight extends CommonFeature {
    @Configurable(
        name = "Light level",
        description = "Amount of light emitted by a Torchflower. Valid values between 0-15."
    )
    public static int lightLevel = 8;

    @Override
    public String description() {
        return "Torchflowers emit ambient light.";
    }

    public static int getLightLevel() {
        return Math.max(0, Math.min(15, lightLevel));
    }
}
