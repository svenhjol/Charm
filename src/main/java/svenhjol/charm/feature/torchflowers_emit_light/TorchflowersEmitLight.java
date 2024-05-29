package svenhjol.charm.feature.torchflowers_emit_light;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.torchflowers_emit_light.common.Handlers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Torchflowers emit ambient light.")
public final class TorchflowersEmitLight extends CommonFeature {
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
