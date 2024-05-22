package svenhjol.charm.feature.crop_replanting;

import svenhjol.charm.feature.crop_replanting.common.Advancements;
import svenhjol.charm.feature.crop_replanting.common.Handlers;
import svenhjol.charm.feature.crop_replanting.common.Providers;
import svenhjol.charm.feature.crop_replanting.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Right-click with a hoe to quickly harvest and replant a fully-grown crop.")
public final class CropReplanting extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;
    public final Advancements advancements;

    public CropReplanting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
        advancements = new Advancements(this);
    }
}
