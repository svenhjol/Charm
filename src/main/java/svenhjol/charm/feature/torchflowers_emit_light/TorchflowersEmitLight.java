package svenhjol.charm.feature.torchflowers_emit_light;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.torchflowers_emit_light.common.Handlers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Torchflowers emit ambient light.")
public class TorchflowersEmitLight extends CommonFeature {
    public final Handlers handlers;

    @Configurable(
        name = "Light level",
        description = "Amount of light emitted by a Torchflower. Valid values between 0-15."
    )
    private static int lightLevel = 8;

    public TorchflowersEmitLight(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    public int lightLevel() {
        return Mth.clamp(lightLevel, 0, 15);
    }
}
