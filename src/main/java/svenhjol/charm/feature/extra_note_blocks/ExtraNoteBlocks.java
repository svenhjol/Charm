package svenhjol.charm.feature.extra_note_blocks;

import svenhjol.charm.feature.extra_note_blocks.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.feature.extra_note_blocks.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature
public final class ExtraNoteBlocks extends CommonFeature {
    public final Handlers handlers;

    public ExtraNoteBlocks(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new AmethystNoteBlock(loader())
        );
    }
}
