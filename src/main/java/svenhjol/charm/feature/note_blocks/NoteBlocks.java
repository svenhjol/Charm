package svenhjol.charm.feature.note_blocks;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.feature.note_blocks.common.Handlers;

import java.util.List;

@Feature(description = """
    Adds more note block sounds.
    Disabling this feature will disable all of Charm's note block features.""")
public final class NoteBlocks extends CommonFeature {
    public final Handlers handlers;

    public NoteBlocks(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.charmony.Feature>> children() {
        return List.of(
            new AmethystNoteBlock(loader())
        );
    }
}
