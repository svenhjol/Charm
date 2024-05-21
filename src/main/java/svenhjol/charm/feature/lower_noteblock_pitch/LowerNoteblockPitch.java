package svenhjol.charm.feature.lower_noteblock_pitch;

import svenhjol.charm.feature.lower_noteblock_pitch.common.Handlers;
import svenhjol.charm.feature.lower_noteblock_pitch.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Use a noteblock while sneaking to lower its pitch by one semitone.")
public final class LowerNoteblockPitch extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public LowerNoteblockPitch(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
