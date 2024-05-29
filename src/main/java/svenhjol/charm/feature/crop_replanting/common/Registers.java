package svenhjol.charm.feature.crop_replanting.common;

import svenhjol.charm.api.event.BlockUseEvent;
import svenhjol.charm.feature.crop_replanting.CropReplanting;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<CropReplanting> {
    public Registers(CropReplanting feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::blockUse);
    }
}
