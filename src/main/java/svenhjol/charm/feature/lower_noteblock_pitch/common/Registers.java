package svenhjol.charm.feature.lower_noteblock_pitch.common;

import svenhjol.charm.api.event.BlockUseEvent;
import svenhjol.charm.feature.lower_noteblock_pitch.LowerNoteblockPitch;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<LowerNoteblockPitch> {
    public Registers(LowerNoteblockPitch feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::useBlock);
    }
}
