package svenhjol.charm.feature.crop_replanting.common;

import svenhjol.charm.charmony.event.BlockUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.crop_replanting.CropReplanting;

public final class Registers extends RegisterHolder<CropReplanting> {
    public Registers(CropReplanting feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::blockUse);
    }
}
