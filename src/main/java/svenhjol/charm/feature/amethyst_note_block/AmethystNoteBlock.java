package svenhjol.charm.feature.amethyst_note_block;

import svenhjol.charm.feature.amethyst_note_block.common.Advancements;
import svenhjol.charm.feature.amethyst_note_block.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;

public class AmethystNoteBlock extends CommonFeature {
    public static final String NOTE_BLOCK_ID = "charm_amethyst";
    public static Registers registers;
    public static Advancements advancements;

    @Override
    public String description() {
        return "Place a block of amethyst under a note block to play its placement sound.";
    }

    @Override
    public void setup() {
        advancements = new Advancements(this);
        registers = new Registers(this);
    }
}
