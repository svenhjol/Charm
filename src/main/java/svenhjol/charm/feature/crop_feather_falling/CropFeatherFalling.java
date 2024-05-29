package svenhjol.charm.feature.crop_feather_falling;

import svenhjol.charm.feature.crop_feather_falling.common.Handlers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Prevents crop trampling when wearing boots enchanted with Feather Falling.")
public final class CropFeatherFalling extends CommonFeature {
    public final Handlers handlers;

    public CropFeatherFalling(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }
}
