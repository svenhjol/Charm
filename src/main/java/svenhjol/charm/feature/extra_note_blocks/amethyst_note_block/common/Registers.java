package svenhjol.charm.feature.extra_note_blocks.amethyst_note_block.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charm.feature.extra_note_blocks.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AmethystNoteBlock> {
    public Supplier<SoundEvent> sound;

    public Registers(AmethystNoteBlock feature) {
        super(feature);

        sound = feature().registry().soundEvent("amethyst");
    }

    /**
     * Sound registration usually happens after the custom note block enum is processed.
     * soundEvent is made accessible so we can safely set it to the registered sound here.
     */
    @Override
    public void onEnabled() {
        var registry = BuiltInRegistries.SOUND_EVENT;

        registry.getResourceKey(sound.get())
            .flatMap(registry::getHolder)
            .ifPresent(
                holder -> feature().parent().handlers.setNoteBlockSound(
                    AmethystNoteBlock.NOTE_BLOCK_ID,
                    holder,
                    NoteBlockInstrument.Type.BASE_BLOCK
                ));
    }
}
