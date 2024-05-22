package svenhjol.charm.feature.crop_feather_falling;

import svenhjol.charm.feature.crop_feather_falling.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Prevents crop trampling when wearing boots enchanted with Feather Falling.")
public class CropFeatherFalling extends CommonFeature {
    public final Handlers handlers;

    public CropFeatherFalling(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }
}
