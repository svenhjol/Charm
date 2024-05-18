package svenhjol.charm.feature.note_blocks.amethyst_note_block;

import svenhjol.charm.feature.note_blocks.NoteBlocks;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.common.Advancements;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Place a block of amethyst under a note block to play its placement sound.")
public final class AmethystNoteBlock extends CommonFeature implements ChildFeature<NoteBlocks> {
    public static final String NOTE_BLOCK_ID = "charm_amethyst";

    public final Registers registers;
    public final Advancements advancements;

    public AmethystNoteBlock(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
    }

    @Override
    public Class<NoteBlocks> typeForParent() {
        return NoteBlocks.class;
    }
}
