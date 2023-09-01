package svenhjol.charm.enums;

import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;

import java.util.List;

public class NoteBlockEnums {
    /**
     * Custom note blocks to inject into the NoteBlockInstrument enum.
     * {@link net.minecraft.world.level.block.state.properties.NoteBlockInstrument}
     */
    public static final List<String> NOTE_BLOCK_ENUMS = List.of(
        AmethystNoteBlock.NOTE_BLOCK_ID
    );
}
