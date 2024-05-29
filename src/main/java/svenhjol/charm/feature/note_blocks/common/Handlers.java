package svenhjol.charm.feature.note_blocks.common;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.note_blocks.NoteBlocks;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.AmethystNoteBlock;

import java.util.List;
import java.util.Locale;

public final class Handlers extends FeatureHolder<NoteBlocks> {
    public Handlers(NoteBlocks feature) {
        super(feature);
    }

    /**
     * Custom note blocks to inject into the NoteBlockInstrument enum.
     * {@link net.minecraft.world.level.block.state.properties.NoteBlockInstrument}
     * Register all feature note block instruments here!
     */
    public static final List<String> NOTE_BLOCK_ENUMS = List.of(
        AmethystNoteBlock.NOTE_BLOCK_ID
    );

    /**
     * Sound registration usually happens after the custom note block enum is processed.
     * soundEvent is made accessible so we can safely set it to the registered sound here.
     */
    public void setNoteBlockSound(String noteBlockName, Holder<SoundEvent> soundEvent, NoteBlockInstrument.Type type) {
        try {
            NoteBlockInstrument noteBlock = NoteBlockInstrument.valueOf(noteBlockName.toUpperCase(Locale.ROOT));
            noteBlock.soundEvent = soundEvent;
            noteBlock.type = type;
        } catch (Exception e) {
            // Don't crash if this fails to assign.
            feature().log().error("Failed to add sound " + soundEvent + " to the noteblock " + noteBlockName + ". Maybe enum mixin was not applied?");
        }
    }
}
