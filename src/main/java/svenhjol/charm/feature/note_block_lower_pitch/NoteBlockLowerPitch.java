package svenhjol.charm.feature.note_block_lower_pitch;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.note_block_lower_pitch.common.Handlers;
import svenhjol.charm.feature.note_block_lower_pitch.common.Registers;

@Feature(description = "Use a note block while sneaking to lower its pitch by one semitone.")
public final class NoteBlockLowerPitch extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public NoteBlockLowerPitch(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
