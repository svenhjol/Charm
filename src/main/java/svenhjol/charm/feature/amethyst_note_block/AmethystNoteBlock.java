package svenhjol.charm.feature.amethyst_note_block;

import svenhjol.charm.feature.amethyst_note_block.common.Advancements;
import svenhjol.charm.feature.amethyst_note_block.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.Setup;

public class AmethystNoteBlock extends CommonFeature {
    public static final String NOTE_BLOCK_ID = "charm_amethyst";

    public final Setup<Registers> registers = Setup.create(this, Registers::new);
    public final Setup<Advancements> advancements = Setup.create(this, Advancements::new);

    public AmethystNoteBlock(CommonLoader loader) {
        super(loader);
    }

    @Override
    public String description() {
        return "Place a block of amethyst under a note block to play its placement sound.";
    }
}
